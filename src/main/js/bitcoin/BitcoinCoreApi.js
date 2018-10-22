"use strict";

class BitcoinCoreApi {

    constructor(client) {
        this.client = client;
    }

    getrawtransaction(event, callback) {

        const timestamp = new Date().getTime();

        //console.log('event=', event);
        const txhash = event.pathParameters.txhash;
        //console.log('txhash=', txhash);
        this.client.getRawTransaction(txhash, false)
            .then((txhex) => {
                //console.log('txhex=', txhex);
                callback(null, JSON.stringify({
                    statusCode: 200,
                    body: txhex
                }));
            })
            .catch((reason) => {
                console.error('getRawTransaction(', txhash, ' reason:', reason);
                callback(null, {
                    statusCode: 400,
                    headers: {"Content-Type": "text/plain"},
                    body: "Couldn't get the txhash.",
                });
            });
    }
}

module.exports = BitcoinCoreApi;