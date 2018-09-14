"use strict";

class Profile {

    constructor(db) {
        this.db = db;
    }

    create(event, callback) {
        const timestamp = new Date().getTime();

        try {
            const data = JSON.parse(event.body);
            if (!data.hasOwnProperty("userName") || data["userName"].length < 1) {data["userName"] = null}
            if (!data.hasOwnProperty("phoneNum") || data["phoneNum"].length < 1) {data["phoneNum"] = null}

            const params = {
                TableName: process.env.PROFILE_TABLE,
                Item: {
                    pubKey: data.pubKey,
                    isArbitrator: data.isArbitrator,
                    userName: data.userName,
                    phoneNum: data.phoneNum,
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
                        body: "Couldn\"t create the profile.",
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
                body: "Couldn\"t parse the profile.",
            });
        }
    }

    list(event, callback) {

        const params = {
            TableName: process.env.PROFILE_TABLE
        };

        // retrieve the offers from the database
        this.db.scan(params, (error, result) => {
            // handle potential errors
            if (error) {
                console.error(error);
                callback(null, {
                    statusCode: error.statusCode || 501,
                    headers: {"Content-Type": "text/plain"},
                    body: "Couldn\"t fetch the profiles.",
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

    update(event, callback) {
        const timestamp = new Date().getTime();

        try {
            const data = JSON.parse(event.body);
            if (!data.hasOwnProperty("userName") || data["userName"].length < 1) {data["userName"] = null}
            if (!data.hasOwnProperty("phoneNum") || data["phoneNum"].length < 1) {data["phoneNum"] = null}

            const params = {
                TableName: process.env.PROFILE_TABLE,
                Key: {
                    pubKey: event.pathParameters.pubKey,
                },
                ExpressionAttributeValues: {
                    ":isArbitrator": data.isArbitrator,
                    ":userName": data.userName,
                    ":phoneNum": data.phoneNum,
                    ":updated": timestamp
                },
                UpdateExpression: "SET isArbitrator = :isArbitrator, userName = :userName, phoneNum = :phoneNum, updated = :updated",
                ReturnValues: "ALL_NEW"
            };

            // write the offer to the database
            this.db.update(params, (error, result) => {
                // handle potential errors
                if (error) {
                    console.error(error);
                    callback(null, {
                        statusCode: error.statusCode || 501,
                        headers: {"Content-Type": "text/plain"},
                        body: "Couldn\"t update the profile.",
                    });
                    return;
                }

                // create a response
                const response = {
                    statusCode: 200,
                    body: JSON.stringify(result.Attributes),
                };
                callback(null, response);
            });
        } catch (err) {
            console.error("Validation Failed: " + err.name);
            callback(null, {
                statusCode: 400,
                headers: {"Content-Type": "text/plain"},
                body: "Couldn\"t parse the profile.",
            });
        }
    }

    delete(event, callback) {

        const params = {
            TableName: process.env.PROFILE_TABLE,
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
                    body: "Couldn\"t delete the profile.",
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

module.exports = Profile;