var currentUrl = window.location.protocol + '//' + window.location.host;

var BASE_URL = window.location.host.includes("localhost:30") ?  "http://localhost:3000/" : currentUrl+"/";
var API_BASE_URL = window.location.host.includes("localhost:30") ?  "http://localhost:9211" : currentUrl;

//here no need to change nothing
var API_URL = API_BASE_URL+"/api/v1/";
var API_ASSETS = API_BASE_URL+"/content/public/";
var ADMIN_BASE_URL = BASE_URL+"cp/#/";
