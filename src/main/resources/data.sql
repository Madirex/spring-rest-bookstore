INSERT INTO clients (id, name, surname, email, phone, street, number, city, province, country, postal_code) VALUES ('6c8aeebf-8e5a-4381-805f-a2494262d6d7','PEPE', 'GARCIA', 'pepe@gmail.com', '123456789', 'Calle Falsa 123', '1', 'Madrid', 'Madrid', 'España', '28001'),
(UUID(), 'JUAN', 'PEREZ', 'juan@gmail.com', '987654321', 'Calle Falsa 321', '1', 'Madrid', 'Madrid', 'España', '28001'),
(UUID(),'MARIA', 'LOPEZ', 'maria@gmail.com', '123456789', 'Calle Falsa 123', '1', 'Madrid', 'Madrid', 'España', '28001');

-- Password para usuarios(admin,user): Nullers123
INSERT INTO bookstore_user(id, name,surnames,username, email, password,created_at,updated_at, is_deleted, roles) VALUES ('6c8aeebf-8e5a-4381-805f-a2494262d6d7','admin','admin','admin','admin@admin.com','$2a$10$yNN.Yn.0ceXjIV7yWUD3sOMUXfvhJzPXCe.L.LnctxyLlWv2mXKSW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,false,'ADMIN'),
                                                                                                                     ('6c8aeebf-8e5a-4381-805f-a2494262d6d6','user','user','user','user@user.com','$2a$10$yNN.Yn.0ceXjIV7yWUD3sOMUXfvhJzPXCe.L.LnctxyLlWv2mXKSW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,false,'USER');