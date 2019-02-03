/*
 * Copyright 2018 Bytabit AB
 *
 */

"use strict";

class Badge {

    constructor(db) {
        this.db = db;
    }

    put(event, callback) {
        const timestamp = new Date().valueOf();

        try {
            const data = JSON.parse(event.body);
            const params = {
                TableName: process.env.BADGE_TABLE,
                Item: {
                    id: data.badge.id,
                    profilePubKey: data.profilePubKey,
                    badgeType: data.badge.badgeType,
                    validFrom: data.badge.validFrom,
                    validTo: data.badge.validTo,
                    currencyCode: data.badge.currencyCode,
                    paymentMethod: data.badge.paymentMethod,
                    detailsHash: data.badge.detailsHash,
                    btcAmount: data.btcAmount,
                    transactionId: data.transactionId,
                    updated: timestamp
                }
            };

            // write the badge to the database
            this.db.put(params, (error) => {
                // handle potential errors
                if (error) {
                    console.error(error);
                    callback(null, {
                        statusCode: error.statusCode || 501,
                        headers: {"Content-Type": "text/plain"},
                        body: "Couldn't put the badge.",
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
                body: "Couldn't parse the badge.",
            });
        }
    }

    getbypubkey(event, callback) {

        const profilePubKeyParam = event["queryStringParameters"]["profilePubKey"];
        const profilePubKey = (profilePubKeyParam === undefined) ? "" : profilePubKeyParam;

        const params = {
            TableName: process.env.BADGE_TABLE,
            IndexName: "badgeIndex",
            ExpressionAttributeValues: {
                ":profilePubKey": profilePubKey
            },
            KeyConditionExpression: "profilePubKey = :profilePubKey",
            ProjectionExpression:"id, profilePubKey, badgeType, validFrom, validTo, currencyCode, paymentMethod, detailsHash"
        };

        // retrieve the trades from the database
        this.db.query(params, (error, result) => {
            // handle potential errors
            if (error) {
                console.error(error);
                callback(null, {
                    statusCode: error.statusCode || 501,
                    headers: {"Content-Type": "text/plain"},
                    body: "Couldn't get the badges.",
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
}

module.exports = Badge;