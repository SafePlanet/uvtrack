'use strict';
var dashboardApp = angular.module("dashboardApp");
dashboardApp.directive('modal', function () {
    return {
        template: '<div class="modal fade">' + '<div class="modal-dialog">' + '<div class="modal-content">' + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' + '<h4 class="modal-title">{{ title }}</h4>' + '</div>'
            + '<div class="modal-body" ng-transclude></div>' + '</div>' + '</div>' + '</div>',
        restrict: 'E',
        transclude: true,
        replace: true,
        scope: true,
        link: function postLink(scope, element, attrs) {
            scope.title = attrs.title;

            scope.$watch(attrs.visible, function (value) {
                if (value == true)
                    $(element).modal('show');
                else
                    $(element).modal('hide');
            });

            $(element).on('shown.bs.modal', function () {
                scope.$apply(function () {
                    scope.$parent[attrs.visible] = true;
                });
            });

            $(element).on('hidden.bs.modal', function () {
                scope.$apply(function () {
                    scope.$parent[attrs.visible] = false;
                });
            });
        }
    };
});

dashboardApp.directive('spiEmail', [function () {
    return {
        require: 'ngModel',
        restrict: 'A',
        scope: {
        },
        link: function (scope, elem, attrs, ctrl) {
            scope.$watch(function () {
                if (ctrl.$viewValue) {
                    //var emailRegEx = /[^\.]([a-zA-Z0-9!#$%&'\*\\+\-\/=\?\^_`\{\}|~\.])*[^\.]*@([a-zA-Z0-9\-]+\.)+[a-zA-Z0-9\-]+/;
                    var emailRegEx = /^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@[-A-Za-z0-9]+(\.[_A-Za-z0-9-]{2,})+$/;
                    var doubleDot = /\.{2}/;
                    return (ctrl.$pristine && angular.isUndefined(ctrl.$viewValue)) || (ctrl.$viewValue.match(emailRegEx) != null && ctrl.$viewValue.match(doubleDot) == null);
                }
                return true;
            }, function (currentValue) {
                ctrl.$setValidity('validEmail', currentValue);
            });
        }
    };
}]);

dashboardApp.directive('numbersOnly', [function () {
    return {
        require: 'ngModel',
        restrict: 'A',
        scope: {
        },
        link: function (scope, element, attrs, modelCtrl) {
            modelCtrl.$parsers.push(function (inputValue) {
                // this next if is necessary for when using ng-required on your input. 
                // In such cases, when a letter is typed first, this parser will be called
                // again, and the 2nd time, the value will be undefined
                if (inputValue == undefined) return '';
                var transformedInput = inputValue.toString().replace(/[^0-9.]/g, '');
                if (transformedInput != inputValue) {
                    modelCtrl.$setViewValue(transformedInput);
                    modelCtrl.$render();
                }

                return transformedInput;
            });
        }
    }
}]);

dashboardApp.directive('toggleSwitch', [
	'$compile',
	function ($compile) {
	    return {
	        restrict: 'EA',
	        replace: true,
	        require: 'ngModel',
	        scope: {
	            isDisabled: '=',
	            onLabel: '@',
	            offLabel: '@',
	            knobLabel: '@',
	            html: '=',
	            onChange: '&'
	        },
	        template: '<div class="ats-switch" ng-click="toggle()" ng-keypress="onKeyPress($event)" ng-class="{ \'disabled\': isDisabled }" role="switch" aria-checked="{{!!model}}">'
                + '<div class="switch-animate" ng-class="{\'switch-off\': !model, \'switch-on\': model}">' + '<span class="switch-left"></span>' + '<span class="knob"></span>'
                + '<span class="switch-right"></span>' + '</div>' + '</div>',
	        compile: function (element, attrs) {
	            if (angular.isUndefined(attrs.onLabel)) {
	                attrs.onLabel = 'On';
	            }
	            if (angular.isUndefined(attrs.offLabel)) {
	                attrs.offLabel = 'Off';
	            }
	            if (angular.isUndefined(attrs.knobLabel)) {
	                attrs.knobLabel = '\u00a0';
	            }
	            if (angular.isUndefined(attrs.isDisabled)) {
	                attrs.isDisabled = false;
	            }
	            if (angular.isUndefined(attrs.html)) {
	                attrs.html = false;
	            }
	            if (angular.isUndefined(attrs.tabindex)) {
	                attrs.tabindex = 0;
	            }

	            return function postLink(scope, iElement, iAttrs, ngModel) {
	                iElement.attr('tabindex', attrs.tabindex);

	                scope.toggle = function toggle() {
	                    if (!scope.isDisabled) {
	                        scope.model = !scope.model;
	                        ngModel.$setViewValue(scope.model);
	                    }
	                    scope.onChange();
	                };

	                var spaceCharCode = 32;
	                scope.onKeyPress = function onKeyPress($event) {
	                    if ($event.charCode == spaceCharCode && !$event.altKey && !$event.ctrlKey && !$event.metaKey) {
	                        scope.toggle();
	                        $event.preventDefault();
	                    }
	                };

	                ngModel.$formatters.push(function (modelValue) {
	                    return modelValue;
	                });

	                ngModel.$parsers.push(function (viewValue) {
	                    return viewValue;
	                });

	                ngModel.$viewChangeListeners.push(function () {
	                    scope.$eval(attrs.ngChange);
	                });

	                ngModel.$render = function () {
	                    scope.model = ngModel.$viewValue;
	                };

	                var bindSpan = function (span, html) {
	                    span = angular.element(span);
	                    var bindAttributeName = (html === true) ? 'ng-bind-html' : 'ng-bind';

	                    // remove old ng-bind attributes
	                    span.removeAttr('ng-bind-html');
	                    span.removeAttr('ng-bind');

	                    if (angular.element(span).hasClass("switch-left"))
	                        span.attr(bindAttributeName, 'onLabel');
	                    if (span.hasClass("knob"))
	                        span.attr(bindAttributeName, 'knobLabel');
	                    if (span.hasClass("switch-right"))
	                        span.attr(bindAttributeName, 'offLabel');

	                    $compile(span)(scope, function (cloned, scope) {
	                        span.replaceWith(cloned);
	                    });
	                };

	                // add ng-bind attribute to each span element.
	                // NOTE: you need angular-sanitize to use ng-bind-html
	                var bindSwitch = function (iElement, html) {
	                    angular.forEach(iElement[0].children[0].children, function (span, index) {
	                        bindSpan(span, html);
	                    });
	                };

	                scope.$watch('html', function (newValue) {
	                    bindSwitch(iElement, newValue);
	                });
	            };
	        }
	    };
	}]);

dashboardApp.directive('spiTypeahead', function () {
    return {
        restict: 'AEC',
        scope: {
            items: '='
        },
        require: 'ngModel',
        link: function (scope, elem, attrs, ngModel) {
            var blur = false;
            scope.focused = false;
            scope.list = [];
            scope.filteredItems = scope.items;
            scope.selPos = 0;

            scope.focusIn = function () {
                if (!scope.focused) {
                    scope.focused = true;
                    blur = false;
                    scope.selPos = 0;
                }
            };

            scope.focusOut = function () {
                scope.itemsearch = "";
                if (!blur) {
                    scope.focused = false;
                } else {
                    angular.element(elem).find('input')[0].focus();
                    blur = false;
                }
            };

            // Change me for custom display name on select list
            scope.getDisplayItem = function (item) {
                return item[attrs.displayfirst] + " " + item[attrs.displaylast];
            };

            // Change me for custom display name on tags (chips)
            scope.getDisplayTag = function (item) {
                return item[attrs.displaytag];
            };

            scope.addItem = function (item) {
                if (item) {
                    scope.list.push(item);
                    scope.itemsearch = "";
                    blur = true;
                    if (scope.selPos >= scope.filteredItems.length - 1) {
                        scope.selPos--; // To keep hover selection
                    }
                    ngModel.$setViewValue(scope.list);
                }
            };

            scope.removeItem = function (item) {
                scope.list.splice(scope.list.indexOf(item), 1);
                ngModel.$setViewValue(scope.list);
            };

            scope.hover = function (index) {
                scope.selPos = index;
            };

            scope.keyPress = function (evt) {
                console.log(evt.keyCode);
                var keys = {
                    38: 'up',
                    40: 'down',
                    8: 'backspace',
                    13: 'enter',
                    9: 'tab',
                    27: 'esc'
                };

                switch (evt.keyCode) {
                    case 27:
                        scope.focusOut();
                        break;
                    case 13:
                        if (scope.selPos > -1) {
                            scope.addItem(scope.filteredItems[scope.selPos]);
                        }
                        break;
                    case 8:
                        if (!scope.itemsearch || scope.itemsearch.length == 0) {
                            if (scope.list.length > 0) {
                                scope.list.pop();
                            }
                        }
                        break;
                    case 38:
                        if (scope.selPos > 0) {
                            scope.selPos--;
                        }
                        break;
                    case 40:
                        if (scope.selPos < scope.filteredItems.length - 1) {
                            scope.selPos++;
                        }
                        break;
                    default:
                        scope.selPos = 0;
                        scope.focusIn();
                }
            };
        },
        templateUrl: 'selectstudent.html'
    };
})
    .directive('focus', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                attrs.$observe('focus', function (newValue) {
                    if (newValue == 'true') {
                        element[0].focus();
                    }
                });
            }
        }
    })
    .filter('notin', function () {
        return function (listin, listout) {
            if (angular.isDefined(listin)) {
                return listin.filter(function (el) {
                    return listout.indexOf(el) == -1;
                });
            }
        };
    })
 .directive('ngTypeahead', ['$log', '$timeout', function ($log, $timeout) {
     return {
         restrict: 'E',
         scope: {
             data: '=',
             delay: "=?",
             forceSelection: "=?",
             limit: '=?',
             startFilter: "=?",
             threshold: '=?',
             onBlur: "=?",
             onSelect: '=?',
             onType: "=?",
             ngModel: '=',
             newLabel: '=?'
         },
         require: "?ngModel",
         transclude: true,
         link: function (scope, elem, attrs, ngModel) {
             var KEY, itemSelected, selectedLabel, selecting, selectedItem;
             KEY = {
                 UP: 38,
                 DOWN: 40,
                 ENTER: 13,
                 TAB: 9,
                 ESC: 27
             };
             var hasFocus = false;

             if (scope.ngModel) {
                 scope.search = scope.ngModel.Name;
                 selectedItem = angular.copy(scope.ngModel);
             }

             ngModel.$render = function () {
                 scope.search = (ngModel.$viewValue && ngModel.$viewValue.Name || '');
             };

             ngModel.$render();

             selectedLabel = scope.search;

             selecting = void 0;
             itemSelected = false;
             scope.index = 0;
             if (!scope.delay) {
                 scope.delay = 0;
             }
             scope.placeholder = attrs.placeholder;
             if (scope.startFilter === void 0) {
                 scope.startFilter = false;
             }
             if (scope.limit === void 0) {
                 scope.limit = Infinity;
             }
             if (scope.threshold === void 0) {
                 scope.threshold = 0;
             }
             if (scope.forceSelection === void 0) {
                 scope.forceSelection = false;
             }

             scope.$onFocus = function () {
                 hasFocus = true;
                 if (scope.threshold == -1) {
                     scope.showSuggestions = true;
                 }
             }

             scope.$watch("search", function (v) {
                 scope.index = 0;

                 if (v === selectedLabel) {
                     return;
                 }

                 ngModel.$setViewValue({ Id: '', Name: v });
                 itemSelected = false;
                 if (v !== void 0) {
                     if (scope.onType) {
                         scope.onType(scope.search);
                     }
                     return scope.showSuggestions = hasFocus && (scope.suggestions.length || scope.newLabel) && scope.search && scope.search.length > scope.threshold;
                 }
             });
             scope.$onBlur = function () {
                 hasFocus = false;
                 if (!itemSelected && scope.forceSelection) {
                     scope.search = selectedLabel;
                     if (selectedItem)
                         ngModel.$setViewValue(selectedItem);
                 }
                 return scope.showSuggestions = false;
             };
             scope.$onSelect = function (item) {
                 ngModel.$setViewValue(item);
                 selecting = true;
                 selectedLabel = item.Name;
                 scope.search = item.Name;
                 selectedItem = item;
                 itemSelected = true;
                 if (scope.onSelect) {
                     scope.onSelect(item);
                 }
                 scope.showSuggestions = false;
                 return $timeout(function () {
                     return selecting = false;
                 });
             };

             return scope.$onKeyDown = function (event) {
                 switch (event.keyCode) {
                     case KEY.UP:
                         if (scope.index > 0) {
                             return scope.index--;
                         } else {
                             return scope.index = scope.suggestions.length - 1;
                         }
                         break;
                     case KEY.DOWN:
                         if (scope.index < (scope.suggestions.length - 1)) {
                             return scope.index++;
                         } else {
                             return scope.index = 0;
                         }
                         break;
                     case KEY.ENTER: {
                         event.preventDefault();
                         return scope.$onSelect(scope.suggestions[scope.index] || scope.ngModel);

                     }
                     case KEY.TAB:
                         return scope.$onSelect(scope.suggestions[scope.index] || scope.ngModel);
                     case KEY.ESC:
                         return scope.showSuggestions = false;
                 }
             };
         },
         template: "<input ng-model=\"search\" placeholder=\"{{placeholder}}\" ng-keydown=\"$onKeyDown($event)\" ng-model-options=\"{ debounce: delay }\" ng-focus=\"$onFocus()\" ng-blur=\"$onBlur()\" class=\"ng-typeahead-input typeahead w-100 form-control\"/>\n<div class=\"ng-typeahead-wrapper\">\n  <ul class=\"ng-typeahead-list\" ng-show=\"showSuggestions\">\n    <li class=\"ng-typeahead-list-item\" ng-repeat=\"item in suggestions = (data | filter:{Name:search} |limitTo: limit | highlight:search:threshold)\" ng-mousedown=\"$onSelect(item)\" ng-class=\"{'active': $index == index}\" ng-bind-html=\"item.html\"></li>\n  \n    <li ng-show=\"newLabel && search.length>0 && (suggestions = (data | filter:{Name:search} |limitTo: limit | highlight:search:threshold)).length==0\" class=\"ng-typeahead-list-item active\">{{newLabel}}</li>\n  </ul>\n</div>\n<div ng-transclude>"
     };
 }]).filter("startsWith", ['$log', function ($log) {
     var strStartsWith;
     strStartsWith = function (suggestion, search, threshold) {
         if (!!suggestion && !!search) {
             console.log('if')
             return suggestion.toLowerCase().indexOf(search.toLowerCase()) === 0;
         } else //if(threshold==-1)
         {
             console.log('else')
             return true;
         }
     };
     return function (suggestions, search, startFilter) {
         var filtered;
         console.log(suggestions)
         if (startFilter) {
             filtered = [];
             angular.forEach(suggestions, function (suggestion) {
                 if (strStartsWith(suggestion.Name, search)) {
                     return filtered.push(suggestion);
                 }
             });
             return filtered;
         } else {
             return suggestions;
         }
     };
 }])
.filter("highlight", ['$sce', function ($sce) {
    return function (item, search, threshold) {
        if (threshold == -1 && !search) search = '0';
        angular.forEach(item, function (input) {
            var exp, highlightedInput, normalInput, words;
            if (search) {
                words = "(" + search.split(/\ /).join(" |") + "|" + search.split(/\ /).join("|") + ")";
                exp = new RegExp(words, "gi");
                normalInput = input.Name.slice(search.length);
                if (words.length) {
                    highlightedInput = input.Name.slice(0, search.length).replace(exp, "<span class=\"ng-typeahead-highlight\">$1</span>");
                }
                return input.html = $sce.trustAsHtml(highlightedInput + normalInput);
            }
        });
        return item;
    };
}]).filter('filterBySchool', function () {
    return function (items, selectedSchoolId) {
        if (!selectedSchoolId) return items;
        var filtered = [];
        angular.forEach(items, function (item) {
            if (item.school && item.school.id == selectedSchoolId) {
                filtered.push(item);
            }
        });
        return filtered;
    };
});