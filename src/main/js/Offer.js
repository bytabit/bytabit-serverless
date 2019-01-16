/*
 * Copyright 2018 Bytabit AB
 *
 */

"use strict";

class Offer {

    constructor(db) {
        this.db = db;
    }

    put(event, callback) {
        const timestamp = new Date().valueOf();

        try {
            const data = JSON.parse(event.body);
            const params = {
                TableName: process.env.OFFER_TABLE,
                Item: {
                    id: data.id,
                    offerType: data.offerType,
                    makerProfilePubKey: data.makerProfilePubKey,
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
                        body: "Couldn\"t put the offer.",
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
                body: "Couldn\"t parse the offer.",
            });
        }
    }

    getall(event, callback) {

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
                    body: "Couldn\"t fetch the offers.",
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

    getbyid(event, callback) {

        const params = {
            TableName: process.env.OFFER_TABLE,
            Key: {
                id: event.pathParameters.id
            }
        };

        // retrieve the offers from the database
        this.db.getItem(params, (error, result) => {
            // handle potential errors
            if (error) {
                console.error(error);
                callback(null, {
                    statusCode: error.statusCode || 501,
                    headers: {"Content-Type": "text/plain"},
                    body: "Couldn\"t fetch the offer.",
                });
                return;
            }

            // create a response
            const response = {
                statusCode: 200,
                body: JSON.stringify(result.Item),
            };
            callback(null, response);
        });
    }

    delete(event, callback) {

        const params = {
            TableName: process.env.OFFER_TABLE,
            Key: {
                id: event.pathParameters.id
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

            const offerId = {
                // deleted offer id
                id: event.pathParameters.id
            };

            // create a response
            const response = {
                statusCode: 200,
                body: JSON.stringify(offerId)
            };
            callback(null, response);
        });
    }
}

module.exports = Offer;