'use strict';

const AWS = require('aws-sdk'); // eslint-disable-line import/no-extraneous-dependencies

const dynamoDb = new AWS.DynamoDB.DocumentClient();

module.exports.create = (event, context, callback) => {
    const timestamp = new Date().getTime();
    const data = JSON.parse(event.body);
    if (typeof data.text !== 'string') {
        console.error('Validation Failed');
        callback(null, {
            statusCode: 400,
            headers: {'Content-Type': 'text/plain'},
            body: 'Couldn\'t create the sell offer.',
        });
        return;
    }

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
        },
    };

    // write the todo to the database
    dynamoDb.put(params, (error) => {
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
};