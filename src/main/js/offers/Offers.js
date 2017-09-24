'use strict';

class Offers {

    constructor(db) {
        this.db = db;
    }

    create(event, callback) {
        const timestamp = new Date().getTime();

        try {
            const data = JSON.parse(event.body);
            const params = {
                TableName: process.env.DYNAMODB_TABLE,
                Item: {
                    sellerEscrowPubKey: data.sellerEscrowPubKey,
                    sellerProfilePubKey: data.sellerProfilePubKey,
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
                        headers: {'Content-Type': 'text/plain'},
                        body: 'Couldn\'t create the sell offer.',
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
            console.error('Validation Failed: '+err.name);
            callback(null, {
                statusCode: 400,
                headers: {'Content-Type': 'text/plain'},
                body: 'Couldn\'t parse the sell offer.',
            });
        }
    }
}

module.exports = Offers;