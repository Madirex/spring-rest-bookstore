db.createUser({
    user: 'admin',
    pwd: 'adminPassword123',
    roles: [
        {
            role: 'readWrite',
            db: 'bookstore',
        },
    ],
});

db = db.getSiblingDB('bookstore');

// Creamos la coleccion city
db.createCollection('orders');


db.orders.insertMany([
    {
        _id: ObjectId('656cf47adfd9e7791750209d'),
        userId: "6c8aeebf-8e5a-4381-805f-a2494262d6d6",
        clientId: "6c8aeebf-8e5a-4381-805f-a2494262d6d7",
        shopId: "ffc16941-8912-4272-ab31-c910e91fd907",
        orderLines: [
            {
                quantity: 2,
                bookId: 1,
                price: 10,
                total: 20
            }
        ],
        total: 20,
        totalBooks: 1,
        createdAt: "2023-10-23T12:57:17.3411925",
        updatedAt: "2023-10-23T12:57:17.3411925",
        isDeleted: false,
        _class: "Order"
    }
]);