-- Para publishers
DROP SEQUENCE IF EXISTS publisher_id_seq;
CREATE SEQUENCE publisher_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1;

-- Para libros
DROP SEQUENCE IF EXISTS books_id_seq;
CREATE SEQUENCE books_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1;

DROP TABLE IF EXISTS "book";
CREATE TABLE "public"."book" (
                                 "id" bigint NOT NULL,
                                 "active" boolean NOT NULL,
                                 "author" character varying(255),
                                 "created_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                 "description" character varying(255),
                                 "image" character varying(255),
                                 "name" character varying(255),
                                 "price" double precision NOT NULL,
                                 "stock" integer,
                                 "updated_at" timestamp(6),
                                 "category_id" uuid,
                                 "publisher_id" bigint NOT NULL,
                                 "book_id" uuid,
                                 CONSTRAINT "book_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

DROP TABLE IF EXISTS "bookstore_user";
CREATE TABLE "public"."bookstore_user" (
                                           "id" uuid NOT NULL,
                                           "created_at" timestamp(6),
                                           "email" character varying(255),
                                           "is_deleted" boolean,
                                           "name" character varying(255) NOT NULL,
                                           "password" character varying(255) NOT NULL,
                                           "surname" character varying(255) NOT NULL,
                                           "updated_at" timestamp(6),
                                           "username" character varying(255) NOT NULL,
                                           CONSTRAINT "bookstore_user_pkey" PRIMARY KEY ("id"),
                                           CONSTRAINT "uk_qnrdfvp8emkt62adbieeqse99" UNIQUE ("username")
) WITH (oids = false);

INSERT INTO "bookstore_user" ("id", "created_at", "email", "is_deleted", "name", "password", "surname", "updated_at", "username") VALUES
                                                                                                                                      ('6c8aeebf-8e5a-4381-805f-a2494262d6d7',	'2023-12-03 20:55:05.695938',	'admin@gmail.com',	'f',	'admin',	'$2a$10$yNN.Yn.0ceXjIV7yWUD3sOMUXfvhJzPXCe.L.LnctxyLlWv2mXKSW',	'admin',	'2023-12-03 20:55:05.695938',	'admin'),
                                                                                                                                      ('6c8aeebf-8e5a-4381-805f-a2494262d6d6',	'2023-12-03 20:55:51.975649',	'user@gmail.com',	'f',	'user',	'$2a$10$yNN.Yn.0ceXjIV7yWUD3sOMUXfvhJzPXCe.L.LnctxyLlWv2mXKSW',	'user',	'2023-12-03 20:55:51.975649',	'user');

DROP TABLE IF EXISTS "categories";
CREATE TABLE "public"."categories" (
                                       "id" uuid NOT NULL,
                                       "created_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                       "is_active" boolean,
                                       "name" character varying(255) NOT NULL,
                                       "updated_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                       CONSTRAINT "categories_pkey" PRIMARY KEY ("id"),
                                       CONSTRAINT "uk_t8o6pivur7nn124jehx7cygw5" UNIQUE ("name")
) WITH (oids = false);

INSERT INTO "categories" ("id", "created_at", "is_active", "name", "updated_at") VALUES
    ('d69cf3db-b77d-4181-b3cd-5ca8107fb6a9',	'2023-12-03 20:52:22.186766',	't',	'Cat 1',	'2023-12-03 20:52:22.186766');

-- -- Agregar Publisher Madirex
-- INSERT INTO "publisher" ("id", "active", "created_at", "image", "name", "updated_at")
-- VALUES (2, 't', CURRENT_TIMESTAMP, 'https://i.imgur.com/dOBv7kY.png', 'Madirex', CURRENT_TIMESTAMP);
--
-- -- Agregar Libro 1
-- INSERT INTO "book" ("id", "active", "author", "created_at", "description", "image", "name", "price", "stock", "updated_at", "category_id", "publisher_id", "book_id")
-- VALUES
--     (2, 't', 'Madirex', CURRENT_TIMESTAMP, 'Unos agentes de investigación reciben la orden de ir a investigar el caso de desaparición de una familia en una mansión abandonada a lo lejos de la ciudad. En el proceso de exploración, los agentes se verán involucrados en diferentes situaciones paranormales. Los protagonistas se darán cuenta de que no están solos en la mansión, en ese momento las cosas se empezarán a complicar. ¿Lograrán los agentes resolver el caso?', 'https://via.placeholder.com/150', 'La Mansión de las Pesadillas', 11.43, 100, CURRENT_TIMESTAMP, 'd69cf3db-b77d-4181-b3cd-5ca8107fb6a9', 2, NULL);
--
-- -- Agregar Libro 2
-- INSERT INTO "book" ("id", "active", "author", "created_at", "description", "image", "name", "price", "stock", "updated_at", "category_id", "publisher_id", "book_id")
-- VALUES
--     (3, 't', 'Madirex', CURRENT_TIMESTAMP, 'Abre la mente, piensa diferente aborda temas que muy poca gente suele pararse a reflexionar. Temas tan delicados como las religiones, la política, las relaciones sociales o incluso la propia muerte. Muchas personas piensan que creen saber cómo funciona la vida, pero ¿esto realmente es así? Vivimos en un mundo extraordinario con cambios exponenciales e inciertos. El desarrollo tecnológico es cada vez mayor y no sabemos qué nos puede llegar a deparar el futuro. ¿Estás preparado para los cambios que vienen?', 'https://via.placeholder.com/150', 'Abre la mente, piensa diferente', 10.40, 100, CURRENT_TIMESTAMP, 'd69cf3db-b77d-4181-b3cd-5ca8107fb6a9', 2, NULL);
--
-- -- Agregar Libro 3
-- INSERT INTO "book" ("id", "active", "author", "created_at", "description", "image", "name", "price", "stock", "updated_at", "category_id", "publisher_id", "book_id")
-- VALUES
--     (4, 't', 'Madirex & DiverInk', CURRENT_TIMESTAMP, 'Manuel es un detective que vive junto a su hijo Toni en el pueblo Risirú. En el pasado, ambos sufrieron la pérdida de un ser querido. La mujer de Manuel había sido asesinada. Pasado un tiempo y con ayuda de profesionales, consiguieron superar el trauma que les había dejado ese asesino. Risirú tenía un pasado muy oscuro, lleno de delincuencia. Manuel consiguió erradicar por completo la mala fama que tenía ese pueblo. Años después... Volvió a morir alguien. Manuel y Toni se preguntaron: ¿El asesino sigue aquí?', 'https://via.placeholder.com/150', '¿El asesino sigue aquí?', 15.18, 100, CURRENT_TIMESTAMP, 'd69cf3db-b77d-4181-b3cd-5ca8107fb6a9', 2, NULL);
--
-- -- Agregar Libro 4
-- INSERT INTO "book" ("id", "active", "author", "created_at", "description", "image", "name", "price", "stock", "updated_at", "category_id", "publisher_id", "book_id")
-- VALUES
--     (5, 't', 'Madirex', CURRENT_TIMESTAMP, 'Este libro no te promete riquezas instantáneas ni cambios mágicos en tu vida económica. Desde mi perspectiva como autor, comparto la filosofía que considero clave para construir una fortuna. Prepárate para un viaje que no transformará tu situación financiera de la noche a la mañana, sino que te guiará por el camino del esfuerzo y la constancia hacia una salud financiera óptima. Te desafiaré a cuestionar paradigmas mentales sobre el manejo del dinero en la sociedad. Explorarás el pasado y el futuro del dinero y las nuevas oportunidades que se vienen gracias a la inteligencia artificial. Prepárate para un viaje transformador hacia la prosperidad financiera.', 'https://via.placeholder.com/150', 'Cóctel de la fortuna', 12, 100, CURRENT_TIMESTAMP, 'd69cf3db-b77d-4181-b3cd-5ca8107fb6a9', 2, NULL);


DROP TABLE IF EXISTS "clients";
CREATE TABLE "public"."clients" (
                                    "id" uuid NOT NULL,
                                    "street" character varying(255),
                                    "number" character varying(255),
                                    "city" character varying(255),
                                    "province" character varying(255),
                                    "country" character varying(255),
                                    "postal_code" character varying(255),
                                    "created_at" timestamp(6),
                                    "email" character varying(255) NOT NULL,
                                    "image" text DEFAULT 'https://via.placeholder.com/150',
                                    "name" character varying(255) NOT NULL,
                                    "phone" character varying(255) NOT NULL,
                                    "surname" character varying(255) NOT NULL,
                                    "client_id" uuid,
                                    CONSTRAINT "clients_pkey" PRIMARY KEY ("id"),
                                    CONSTRAINT "uk_srv16ica2c1csub334bxjjb59" UNIQUE ("email")
) WITH (oids = false);

INSERT INTO "clients" ("id", "street", "number", "city", "province", "country", "postal_code", "created_at", "email", "image", "name", "phone", "surname", "client_id") VALUES
    ('6c8aeebf-8e5a-4381-805f-a2494262d6d7',	'Calle 1',	'1',	'Leganes',	'Madrid',	'España',	'28970',	'2023-12-03 20:58:09.960745',	'client@gmail.com',	'https://via.placeholder.com/150',	'Client',	'633331367',	'client',	NULL);

DROP TABLE IF EXISTS "publisher";
CREATE TABLE "public"."publisher" (
                                      "id" bigint NOT NULL,
                                      "active" boolean,
                                      "created_at" timestamp(6),
                                      "image" character varying(255),
                                      "name" character varying(255),
                                      "updated_at" timestamp(6),
                                      CONSTRAINT "publisher_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

DROP TABLE IF EXISTS "shops";
CREATE TABLE "public"."shops" (
                                  "id" uuid NOT NULL,
                                  "created_at" timestamp(6),
                                  "street" character varying(200),
                                  "number" character varying(255),
                                  "city" character varying(255),
                                  "province" character varying(255),
                                  "country" character varying(255),
                                  "postal_code" character varying(255),
                                  "name" character varying(255),
                                  "updated_at" timestamp(6),
                                  CONSTRAINT "shops_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

INSERT INTO "shops" ("id", "created_at", "street", "number", "city", "province", "country", "postal_code", "name", "updated_at") VALUES
    ('ffc16941-8912-4272-ab31-c910e91fd907',	'2023-12-03 20:59:11.870729',	'Calle 1',	'1',	'Leganes',	'Madrid',	'España',	'28970',	'Shop 1',	'2023-12-03 20:59:11.870729');

DROP TABLE IF EXISTS "user_roles";
CREATE TABLE "public"."user_roles" (
                                       "user_id" uuid NOT NULL,
                                       "roles" character varying(255)
) WITH (oids = false);

INSERT INTO "user_roles" ("user_id", "roles") VALUES
                                                  ('6c8aeebf-8e5a-4381-805f-a2494262d6d6',	'USER'),
                                                  ('6c8aeebf-8e5a-4381-805f-a2494262d6d7',	'USER'),
                                                  ('6c8aeebf-8e5a-4381-805f-a2494262d6d7',	'ADMIN');

ALTER TABLE ONLY "public"."book" ADD CONSTRAINT "fk7jv5rwmalxg0a02a3ublrk0j2" FOREIGN KEY (category_id) REFERENCES categories(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."book" ADD CONSTRAINT "fkgtvt7p649s4x80y6f4842pnfq" FOREIGN KEY (publisher_id) REFERENCES publisher(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."book" ADD CONSTRAINT "fkq3kvfof22chvr9yfk3cb09whi" FOREIGN KEY (book_id) REFERENCES shops(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."clients" ADD CONSTRAINT "fk9cv9qa5lbqibnrw8tawj69tss" FOREIGN KEY (client_id) REFERENCES shops(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."user_roles" ADD CONSTRAINT "fkblcajsxsv8orxflma5gnt4j7k" FOREIGN KEY (user_id) REFERENCES bookstore_user(id) NOT DEFERRABLE;