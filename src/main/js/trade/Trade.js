'use strict';

class Trade {

    constructor(db) {
        this.db = db;
    }

    create(event, callback) {
        const timestamp = new Date().getTime();

        try {
            const data = JSON.parse(event.body);
            const escrowAddress = event.pathParameters.escrowAddress;

            const params = {
                TableName: process.env.TRADE_TABLE,
                Item: {
                    // Sell Offer
                    escrowAddress: escrowAddress,
                    sellerEscrowPubKey: data.sellerEscrowPubKey,
                    sellerProfilePubKey: data.sellerProfilePubKey,
                    arbitratorProfilePubKey: data.arbitratorProfilePubKey,
                    currencyCode: data.currencyCode,
                    paymentMethod: data.paymentMethod,
                    minAmount: data.minAmount,
                    maxAmount: data.maxAmount,
                    price: data.price,

                    // Buy Request
                    buyerEscrowPubKey: data.buyerEscrowPubKey,
                    btcAmount: data.btcAmount,
                    buyerProfilePubKey: data.buyerProfilePubKey,
                    buyerPayoutAddress: data.buyerPayoutAddress,

                    // Payment Request
                    fundingTxHash: data.fundingTxHash,
                    paymentDetails: data.paymentDetails,
                    refundAddress: data.refundAddress,
                    refundTxSignature: data.refundTxSignature,

                    // Payout Request
                    paymentReference: data.paymentReference,
                    payoutTxSignature: data.payoutTxSignature,

                    // Arbitrate Request
                    arbitrationReason: data.arbitrationReason,

                    // Payout Completed
                    payoutTxHash: data.payoutTxHash,
                    payoutReason: data.payoutReason,

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
                        body: 'Couldn\'t put the trade.',
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
            console.error('Validation Failed: ' + err.name);
            callback(null, {
                statusCode: 400,
                headers: {'Content-Type': 'text/plain'},
                body: 'Couldn\'t parse the trade.',
            });
        }
    }

    list(event, callback) {

        const profilePubKey = event["queryStringParameters"]['profilePubKey'];

        function params(indexName, keyName) {
            return {
                TableName: process.env.TRADE_TABLE,
                IndexName: indexName,
                ExpressionAttributeValues: {
                    ":pubKey": profilePubKey
                },
                KeyConditionExpression: keyName + "= :pubKey"
            };
        }

        const sellerPromise = new Promise((resolve, reject) => {
            // retrieve the trades from the database
            this.db.query(params('sellerIndex', 'sellerProfilePubKey'), (error, result) => {
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
            this.db.query(params('buyerIndex', 'buyerProfilePubKey'), (error, result) => {
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
            this.db.query(params('arbitratorIndex', 'arbitratorProfilePubKey'), (error, result) => {
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
                headers: {'Content-Type': 'text/plain'},
                body: 'Couldn\'t fetch the trades by sellerProfilePubKey.',
            });
        });
    }
}

module.exports = Trade;