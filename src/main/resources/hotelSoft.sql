
CREATE TABLE employees
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name text NOT NULL,
    position text NOT NULL,
    email character varying NOT NULL ,
    hotel_id integer,
    CONSTRAINT pk_employees_id PRIMARY KEY (id),
    CONSTRAINT fk_hotel_id FOREIGN KEY (hotel_id)
        REFERENCES public.hotels (id)

)

INSERT INTO employee (
   id,name,position,email,hotel_id)
VALUES (,,,);


CREATE TABLE guests
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 1000 CACHE 1 ),
    name text NOT NULL ,
    email character varying NOT NULL ,
    CONSTRAINT guests_key PRIMARY KEY (id),
    CONSTRAINT fk_reservations_id FOREIGN KEY (id)
        REFERENCES public.reservations (id)
)

CREATE TABLE hotels
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name text NOT NULL ,
    address character varying(100) NOT NULL ,
    CONSTRAINT hotels_key PRIMARY KEY (id)
)


CREATE TABLE payments
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    amount integer NOT NULL,
    payment_method character varying NOT NULL,
    CONSTRAINT payment_id PRIMARY KEY (id)
)
CREATE TABLE reservations
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    reservation_id integer NOT NULL,
    check_in_date date NOT NULL,
    check_out_date date NOT NULL,
    allocated_room integer NOT NULL,
    room_id integer NOT NULL,
    payment_id integer NOT NULL,
    guest_id integer NOT NULL,
    CONSTRAINT reservations_pkey PRIMARY KEY (id),
    CONSTRAINT fk_guests_id FOREIGN KEY (guest_id)
        REFERENCES guests (id),
    CONSTRAINT fk_payment_id FOREIGN KEY (payment_id)
        REFERENCES payments (id),
    CONSTRAINT fk_room_id FOREIGN KEY (room_id)
        REFERENCES rooms (id)
  )

CREATE TABLE IF NOT EXISTS public.rooms
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    room_number integer NOT NULL,
    floor integer NOT NULL,
    room_type character varying NOT NULL,
    price_per_night character varying  NOT NULL,
    available character varying  NOT NULL,
    hotel_id integer NOT NULL,
    CONSTRAINT rooms_pkey PRIMARY KEY (id),
    CONSTRAINT fk_hotel_id FOREIGN KEY (hotel_id)
        REFERENCES hotels (id)
)