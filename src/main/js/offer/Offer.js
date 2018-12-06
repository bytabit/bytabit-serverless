"use strict";

class Offer {

    constructor(db) {
        this.db = db;
    }

    create(event, callback) {
        const timestamp = new Date().valueOf();

        try {
            const data = JSON.parse(event.body);
            const params = {
                TableName: process.env.OFFER_TABLE,
                Item: {
                    offerType: data.offerType,
                    traderEscrowPubKey: data.traderEscrowPubKey,
                    traderProfilePubKey: data.traderProfilePubKey,
                    arbitratorProfilePubKey: data.arbitratorProfilePubKey,
                    currencyCode: data.currencyCode,
                    paymentMethod: data.paymentMethod,
                    minAmount: data.minAmount,
                    maxAmount: data.maxAmount,
                    price: data.price,
                    created: timestamp,
                    updated: timestamp
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
                        body: "Couldn\"t create the sell offer.",
                    });
                    return;
                }

                // create a response
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
                body: "Couldn\"t parse the sell offer.",
            });
        }
    }

    list(event, callback) {

        const params = {
            TableName: process.env.OFFER_TABLE
        };

        // retrieve the offers from the database
        this.db.scan(params, (error, result) => {
            // handle potential errors
            if (error) {
                console.error(error);
                callback(null, {
                    statusCode: error.statusCode || 501,
                    headers: {"Content-Type": "text/plain"},
                    body: "Couldn\"t fetch the sell offer.",
                });
                return;
            }

            // create a response
            const response = {
                statusCode: 200,
                body: JSON.stringify(result.Items),
            };
            callback(null, response);
        });
    }

    delete(event, callback) {

        const params = {
            TableName: process.env.OFFER_TABLE,
            Key: {
                sellerEscrowPubKey: event.pathParameters.sellerEscrowPubKey
            }
        };

        // delete the offer from the database
        this.db.delete(params, (error) => {
            // handle potential errors
            if (error) {
                console.error(error);
                callback(null, {
                    statusCode: error.statusCode || 501,
                    headers: {"Content-Type": "text/plain"},
                    body: "Couldn\"t delete the sell offer.",
                });
                return;
            }

            // create a response
            const response = {
                statusCode: 200,
                body: JSON.stringify({}),
            };
            callback(null, response);
        });
    }
}

module.exports = Offer;