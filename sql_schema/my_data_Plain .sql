--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2024-12-23 14:07:00

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 16398)
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    price numeric(10,2) NOT NULL
);


ALTER TABLE public.products OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16397)
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.products_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.products_id_seq OWNER TO postgres;

--
-- TOC entry 4829 (class 0 OID 0)
-- Dependencies: 219
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;


--
-- TOC entry 222 (class 1259 OID 16405)
-- Name: sales; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sales (
    id integer NOT NULL,
    seller_id integer NOT NULL,
    product_id integer NOT NULL,
    quantity integer NOT NULL,
    sale_date date DEFAULT CURRENT_DATE
);


ALTER TABLE public.sales OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16404)
-- Name: sales_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sales_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sales_id_seq OWNER TO postgres;

--
-- TOC entry 4830 (class 0 OID 0)
-- Dependencies: 221
-- Name: sales_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sales_id_seq OWNED BY public.sales.id;


--
-- TOC entry 218 (class 1259 OID 16389)
-- Name: sellers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sellers (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    contact_info text
);


ALTER TABLE public.sellers OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16388)
-- Name: sellers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sellers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sellers_id_seq OWNER TO postgres;

--
-- TOC entry 4831 (class 0 OID 0)
-- Dependencies: 217
-- Name: sellers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sellers_id_seq OWNED BY public.sellers.id;


--
-- TOC entry 224 (class 1259 OID 16440)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    role character varying(50) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16439)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 4832 (class 0 OID 0)
-- Dependencies: 223
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 4657 (class 2604 OID 16401)
-- Name: products id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);


--
-- TOC entry 4658 (class 2604 OID 16408)
-- Name: sales id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales ALTER COLUMN id SET DEFAULT nextval('public.sales_id_seq'::regclass);


--
-- TOC entry 4656 (class 2604 OID 16392)
-- Name: sellers id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sellers ALTER COLUMN id SET DEFAULT nextval('public.sellers_id_seq'::regclass);


--
-- TOC entry 4660 (class 2604 OID 16443)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 4819 (class 0 OID 16398)
-- Dependencies: 220
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.products (id, name, price) FROM stdin;
5	iPad	199.99
2	Ivan	100.00
1	Laptop	200.00
4	Smart	99.99
8	Ipad	250.00
3	Ipad	550.00
\.


--
-- TOC entry 4821 (class 0 OID 16405)
-- Dependencies: 222
-- Data for Name: sales; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sales (id, seller_id, product_id, quantity, sale_date) FROM stdin;
2	3	1	5	2024-12-20
1	1	5	2	2024-12-20
3	2	4	7	2024-12-20
4	1	8	3	2024-12-20
\.


--
-- TOC entry 4817 (class 0 OID 16389)
-- Dependencies: 218
-- Data for Name: sellers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sellers (id, name, contact_info) FROM stdin;
3	Ivan	ivan@gmail.com
1	Ivan	ivan@gmail.com
4	Ivanov	ivanov@example.com
2	Greny	greny@gmail.com
\.


--
-- TOC entry 4823 (class 0 OID 16440)
-- Dependencies: 224
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, username, password, role) FROM stdin;
2	admin	adminpass	ADMIN
1	user	1234	USER
3	admin2	adminpass2	ADMIN
\.


--
-- TOC entry 4833 (class 0 OID 0)
-- Dependencies: 219
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.products_id_seq', 11, true);


--
-- TOC entry 4834 (class 0 OID 0)
-- Dependencies: 221
-- Name: sales_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sales_id_seq', 6, true);


--
-- TOC entry 4835 (class 0 OID 0)
-- Dependencies: 217
-- Name: sellers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sellers_id_seq', 6, true);


--
-- TOC entry 4836 (class 0 OID 0)
-- Dependencies: 223
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 2, true);


--
-- TOC entry 4664 (class 2606 OID 16403)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- TOC entry 4666 (class 2606 OID 16411)
-- Name: sales sales_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales
    ADD CONSTRAINT sales_pkey PRIMARY KEY (id);


--
-- TOC entry 4662 (class 2606 OID 16396)
-- Name: sellers sellers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sellers
    ADD CONSTRAINT sellers_pkey PRIMARY KEY (id);


--
-- TOC entry 4668 (class 2606 OID 16447)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4669 (class 2606 OID 16417)
-- Name: sales fk_product; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales
    ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 4670 (class 2606 OID 16412)
-- Name: sales fk_seller; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales
    ADD CONSTRAINT fk_seller FOREIGN KEY (seller_id) REFERENCES public.sellers(id);


-- Completed on 2024-12-23 14:07:01

--
-- PostgreSQL database dump complete
--

