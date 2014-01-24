var AmazonInAppPurchasing = function() {};
var serviceName = 'AmazonInAppPurchasing';

/**
Initializes the in-app purchasing SDK.
*/
AmazonInAppPurchasing.prototype.initialize = function (callback, errback) {
	return cordova.exec(callback, errback, serviceName, 'initialize', []);
};

/**
Gets the logged in user Amazon Customer Id
*/
AmazonInAppPurchasing.prototype.getUserId = function (callback, errback) {
	return cordova.exec(callback, errback, serviceName, 'userId', []);
};

/**
Gets the item data given a set of skus
*/
// skus = an array of skus
// e.g. [sku1, sku2]
AmazonInAppPurchasing.prototype.getItemData = function (skus, callback, errback) {
	return cordova.exec(callback, errback, serviceName, 'itemData', [skus]);
};

/**
Purchases a given sku
*/
// sku represents the sku of the item
AmazonInAppPurchasing.prototype.purchase = function (sku, callback, errback) {
	return cordova.exec(callback, errback, serviceName, 'purchase', [sku]);
};

/**
Gets the purchase updates since the last offset.
*/
// offset represents the last purchase offset
// if you want to get all purchase updates, make the request with offset = null
AmazonInAppPurchasing.prototype.getPurchaseUpdates = function (offset, callback, errback) {
	var args = offset ? [offset] : [];
	return cordova.exec(callback, errback, serviceName, 'purchaseUpdates', args);
};

module.exports = new AmazonInAppPurchasing();
