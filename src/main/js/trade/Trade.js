"use strict";

class Trade {

    constructor(db) {
        this.db = db;
    }

    create(event, callback) {
        const timestamp = new Date().getTime();

        try {
            const tradeServiceResource = JSON.parse(event.body);
            const escrowAddress = event.pathParameters.escrowAddress;

            const params = {
                TableName: process.env.TRADE_TABLE,
                Item: {
                    // search keys
                    escrowAddress: escrowAddress,
                    version: timestamp,
                    sellerProfilePubKey: tradeServiceResource.sellerProfilePubKey,
                    buyerProfilePubKey: tradeServiceResource.buyerProfilePubKey,
                    arbitratorProfilePubKey: tradeServiceResource.arbitratorProfilePubKey,

                    // TODO this will be block of encrypted trade data
                    trade: tradeServiceResource.trade
                }
            };

            // write the offer to the database
            this.db.put(params, (error) => {
                // handle potential errors
                if (error) {
                    console.error(error);
                    callback(null, {
                        statusCode: error.statusCode || 501,
                        headers: {"Content-Type": "text/plain"},
                        body: "Couldn\"t put the trade.",
                    });
                    return;
                }

                // put a response
                const response = {
                    statusCode: 200,
                    body: JSON.stringify(params.Item),
                };
                callback(null, response);
            });
        } catch (err) {
            console.error("Validation Failed: " + err.name);
            callback(null, {
                statusCode: 400,
                headers: {"Content-Type": "text/plain"},
                body: "Couldn\"t parse the trade.",
            });
        }
    }

    list(event, callback) {

        const profilePubKey = event["queryStringParameters"]["profilePubKey"];

        const versionParam = event["queryStringParameters"]["version"];
        const version = (versionParam === undefined) ? 0 : versionParam;

        function params(indexName, keyName) {
            return {
                TableName: process.env.TRADE_TABLE,
                IndexName: indexName,
                ExpressionAttributeValues: {
                    ":pubKey": profilePubKey,
                    ":version": version
                },
                KeyConditionExpression: keyName + "= :pubKey",
                FilterExpression: "version > :version"
            };
        }

        const sellerPromise = new Promise((resolve, reject) => {
            // retrieve the trades from the database
            this.db.query(params("sellerIndex", "sellerProfilePubKey"), (error, result) => {
                // handle potential errors
                if (error) {
                    console.error(error);
                    reject(error);
                }
                resolve(result.Items);
            });
        });

        const buyerPromise = new Promise((resolve, reject) => {
            // retrieve the trades from the database
            this.db.query(params("buyerIndex", "buyerProfilePubKey"), (error, result) => {
                // handle potential errors
                if (error) {
                    console.error(error);
                    reject(error);
                }
                resolve(result.Items);
            });
        });

        const arbitratorPromise = new Promise((resolve, reject) => {
            // retrieve the trades from the database
            this.db.query(params("arbitratorIndex", "arbitratorProfilePubKey"), (error, result) => {
                // handle potential errors
                if (error) {
                    console.error(error);
                    reject(error);
                }
                resolve(result.Items);
            });
        });

        Promise.all([sellerPromise, buyerPromise, arbitratorPromise]).then(items => {
            // if resolve
            const response = {
                statusCode: 200,
                body: JSON.stringify([].concat.apply([], items))
            };
            callback(null, response);
        }).catch(error => {
            // if reject
            callback(null, {
                statusCode: error.statusCode || 501,
                headers: {"Content-Type": "text/plain"},
                body: "Couldn\"t fetch the trades by profilePubKey.",
            });
        });
    }
}

module.exports = Trade;