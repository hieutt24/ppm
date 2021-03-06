'use strict';

angular.module('ppmApp')
    .service('DateUtils', function () {
      this.convertLocaleDateToServer = function(date) {
//        if (date) {
//          var utcDate = new Date();
//          utcDate.setUTCDate(date.getDate());
//          utcDate.setUTCMonth(date.getMonth());
//          utcDate.setUTCFullYear(date.getFullYear());
//          return utcDate;
//        } else {
//          return null;
//        }
    	  if (date.indexOf("/") >= 0) {
	    	  var pattern = /^(\d{1,2})\/(\d{1,2})\/(\d{4})$/;
	          var arrayDate = date.match(pattern);
	          var dt = new Date(arrayDate[3], arrayDate[2] - 1, arrayDate[1]);
	          return dt;
    	  } else {
    		  var pattern = /^(\d{4})-(\d{1,2})-(\d{1,2})$/;
	          var arrayDate = date.match(pattern);
	          var dt = new Date(arrayDate[1], arrayDate[2] - 1, arrayDate[3]);
	          return dt;
    	  }
      };
      this.convertLocaleDateFromServer = function(date) {
        if (date) {
          //var dateString = date.split("-");
          //return new Date(dateString[0], dateString[1] - 1, dateString[2]);
          var dt = new Date(date);var m = dt.getMonth() + 1;
          return dt.getFullYear() + "-" + m + "-" + dt.getDate();
        }
        return null;
      };
      this.convertDateTimeFromServer = function(date) {
        if (date) {
          return new Date(date);   
        } else {
          return null;
        }
      }
    });
