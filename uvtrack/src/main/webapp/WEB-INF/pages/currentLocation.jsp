<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
{
 "markers": {
            <c:forEach var="latlongs" items="${it.get(0).currentLatlongs}" varStatus="arrIndex">
              "cur${arrIndex.index}":{"lat":${latlongs[0]}, "lng":${latlongs[1]}, "message": "${latlongs[2]}", "icon":{"iconUrl": "icons/bus.svg", "iconSize": [24, 24], "iconAnchor": [12, 24], "popupAnchor": [0, 0], "shadowSize": [0, 0], "shadowAnchor": [0, 0]}}${!arrIndex.last ? ',' : ''}
            </c:forEach>
    }
}
