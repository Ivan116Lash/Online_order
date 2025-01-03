PGDMP      ;                |            mydata    17.2    17.2 "    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    16387    mydata    DATABASE     }   CREATE DATABASE mydata WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Ukrainian_Ukraine.1251';
    DROP DATABASE mydata;
                     postgres    false            �            1259    16398    products    TABLE     �   CREATE TABLE public.products (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    price numeric(10,2) NOT NULL
);
    DROP TABLE public.products;
       public         heap r       postgres    false            �            1259    16397    products_id_seq    SEQUENCE     �   CREATE SEQUENCE public.products_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.products_id_seq;
       public               postgres    false    220            �           0    0    products_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;
          public               postgres    false    219            �            1259    16405    sales    TABLE     �   CREATE TABLE public.sales (
    id integer NOT NULL,
    seller_id integer NOT NULL,
    product_id integer NOT NULL,
    quantity integer NOT NULL,
    sale_date date DEFAULT CURRENT_DATE
);
    DROP TABLE public.sales;
       public         heap r       postgres    false            �            1259    16404    sales_id_seq    SEQUENCE     �   CREATE SEQUENCE public.sales_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.sales_id_seq;
       public               postgres    false    222            �           0    0    sales_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.sales_id_seq OWNED BY public.sales.id;
          public               postgres    false    221            �            1259    16389    sellers    TABLE     z   CREATE TABLE public.sellers (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    contact_info text
);
    DROP TABLE public.sellers;
       public         heap r       postgres    false            �            1259    16388    sellers_id_seq    SEQUENCE     �   CREATE SEQUENCE public.sellers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.sellers_id_seq;
       public               postgres    false    218            �           0    0    sellers_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.sellers_id_seq OWNED BY public.sellers.id;
          public               postgres    false    217            �            1259    16440    users    TABLE     �   CREATE TABLE public.users (
    id integer NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    role character varying(50) NOT NULL
);
    DROP TABLE public.users;
       public         heap r       postgres    false            �            1259    16439    users_id_seq    SEQUENCE     �   CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.users_id_seq;
       public               postgres    false    224            �           0    0    users_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;
          public               postgres    false    223            1           2604    16401    products id    DEFAULT     j   ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);
 :   ALTER TABLE public.products ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    220    219    220            2           2604    16408    sales id    DEFAULT     d   ALTER TABLE ONLY public.sales ALTER COLUMN id SET DEFAULT nextval('public.sales_id_seq'::regclass);
 7   ALTER TABLE public.sales ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    222    221    222            0           2604    16392 
   sellers id    DEFAULT     h   ALTER TABLE ONLY public.sellers ALTER COLUMN id SET DEFAULT nextval('public.sellers_id_seq'::regclass);
 9   ALTER TABLE public.sellers ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    217    218    218            4           2604    16443    users id    DEFAULT     d   ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);
 7   ALTER TABLE public.users ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    224    223    224            �          0    16398    products 
   TABLE DATA           3   COPY public.products (id, name, price) FROM stdin;
    public               postgres    false    220   �#       �          0    16405    sales 
   TABLE DATA           O   COPY public.sales (id, seller_id, product_id, quantity, sale_date) FROM stdin;
    public               postgres    false    222   �#       �          0    16389    sellers 
   TABLE DATA           9   COPY public.sellers (id, name, contact_info) FROM stdin;
    public               postgres    false    218   H$       �          0    16440    users 
   TABLE DATA           =   COPY public.users (id, username, password, role) FROM stdin;
    public               postgres    false    224   �$       �           0    0    products_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.products_id_seq', 11, true);
          public               postgres    false    219            �           0    0    sales_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.sales_id_seq', 6, true);
          public               postgres    false    221            �           0    0    sellers_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.sellers_id_seq', 6, true);
          public               postgres    false    217            �           0    0    users_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.users_id_seq', 2, true);
          public               postgres    false    223            8           2606    16403    products products_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.products DROP CONSTRAINT products_pkey;
       public                 postgres    false    220            :           2606    16411    sales sales_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.sales
    ADD CONSTRAINT sales_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.sales DROP CONSTRAINT sales_pkey;
       public                 postgres    false    222            6           2606    16396    sellers sellers_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.sellers
    ADD CONSTRAINT sellers_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.sellers DROP CONSTRAINT sellers_pkey;
       public                 postgres    false    218            <           2606    16447    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public                 postgres    false    224            =           2606    16417    sales fk_product    FK CONSTRAINT     u   ALTER TABLE ONLY public.sales
    ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES public.products(id);
 :   ALTER TABLE ONLY public.sales DROP CONSTRAINT fk_product;
       public               postgres    false    220    222    4664            >           2606    16412    sales fk_seller    FK CONSTRAINT     r   ALTER TABLE ONLY public.sales
    ADD CONSTRAINT fk_seller FOREIGN KEY (seller_id) REFERENCES public.sellers(id);
 9   ALTER TABLE ONLY public.sales DROP CONSTRAINT fk_seller;
       public               postgres    false    222    4662    218            �   R   x�3��HL�4��Գ��2��,K��440�30�2��I,(�/�4��M8�s�J8!j-8=�:�L�r��)�gh����� 	      �   <   x�]��� ��������_G@<<w�FB�p��8�ʑ�q�M��(�7��V�7��      �   I   x�3��,K���鹉�9z���\�XEM���e`��2�Ԋ�܂�T���{Qj^%g:�D6��.�=... ��1�      �   U   x�3�LL���3�*I7��tt����2�,-N-����s���v�2�(��5-��𫂪7�7�L���I��j����� ���     