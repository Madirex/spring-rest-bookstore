db.createUser({
    user: 'admin',
    pwd: 'adminPassword123',
    roles: [
        {
            role: 'readWrite',
            db: 'shop',
        },
    ],
});