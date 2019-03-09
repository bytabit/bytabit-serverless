/*
 * Copyright 2019 Bytabit AB
 */

const Offer = require("../../../main/js/offer/offer");

test("parse event.body", () => {

    const data1 = JSON.parse("{ "name":"John", "age":30, "city":"New York"}");
    expect(data1.name).toBe("John");

    expect(() => JSON.parse("hello")).toThrow(SyntaxError);
});

test("create offer with valid input", () => {

    const db = {
      put: (params, handler) => {
         expect(params.Item.currencyCode).toBe("SEK");
         handler();
      }
    };

    let offer = new Offer(db);

    const event = {
        body: "{"arbitratorProfilePubKey":"fTiQZ15v1jYmyvHNZhrzP9DiYosGTqJ23NUfHom7q4No","complete":true,"currencyCode":"SEK","maxAmount":600.00,"minAmount":300.00,"paymentMethod":"SWISH","price":10000.00,"sellerEscrowPubKey":"29GujoVDK8Eqokcwx3b1oNyVYQVwRUYB4R7WoF7az8c87","sellerProfilePubKey":"vKiUC97W5UDxywV9SPzYvQ8Ri9Hxm9QJyeNFLCtjpqfV"}"
    };

    offer.create(event, (param1, response) => {
        expect(response.statusCode).toBe(200);
    })
});