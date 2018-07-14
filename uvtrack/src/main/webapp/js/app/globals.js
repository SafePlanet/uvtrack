'use strict';

function makeRandomString() {
    return Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
}

function getAuthToken(Url, time, nonce) {

    // var string_to_hash = javaRest.cookie.get('token') + ':' +
    // Url+javaRest.cookie.get('userId') + ',GET,' + time + "," + nonce;
    var string_to_hash = javaRest.cookie.get('token') + ':' + Url + ',GET,' + time + "," + nonce;
    var authorization = javaRest.cookie.get('userId') + ':' + javaRest.hash(string_to_hash);
    return authorization;
}

function getPostAuthToken(Url, time, nonce) {

    // var string_to_hash = javaRest.cookie.get('token') + ':' +
    // Url+javaRest.cookie.get('userId') + ',POST,' + time + "," + nonce;
    var string_to_hash = javaRest.cookie.get('token') + ':' + Url + javaRest.cookie.get('userId') + ',POST,' + time + "," + nonce;
    var authorization = javaRest.cookie.get('userId') + ':' + javaRest.hash(string_to_hash);
    return authorization;
}

function getPutAuthToken(Url, time, nonce) {

    // var string_to_hash = javaRest.cookie.get('token') + ':' +
    // Url+javaRest.cookie.get('userId') + ',POST,' + time + "," + nonce;
    var string_to_hash = javaRest.cookie.get('token') + ':' + Url + javaRest.cookie.get('userId') + ',PUT,' + time + "," + nonce;
    var authorization = javaRest.cookie.get('userId') + ':' + javaRest.hash(string_to_hash);
    return authorization;
}

function replaceAll(str, find, replace) {
    return str.replace(new RegExp(escapeRegExp(find), 'g'), replace);
}

function escapeRegExp(str) {
    return str.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
}

function addZero(i) {
    if (i < 10) {
	i = "0" + i;
    }
    return i;
}

Date.prototype.getTimeString = function() {
    return this.getHours() + ":" + this.getMinutes() + ":" + this.getSeconds();
}
