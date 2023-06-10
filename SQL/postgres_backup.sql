--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3
-- Dumped by pg_dump version 15.3

-- Started on 2023-06-10 16:38:07

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
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
-- TOC entry 225 (class 1259 OID 33240)
-- Name: bundles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bundles (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    plugins bigint[] NOT NULL
);


ALTER TABLE public.bundles OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 33239)
-- Name: bundles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bundles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bundles_id_seq OWNER TO postgres;

--
-- TOC entry 3390 (class 0 OID 0)
-- Dependencies: 224
-- Name: bundles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.bundles_id_seq OWNED BY public.bundles.id;


--
-- TOC entry 215 (class 1259 OID 33176)
-- Name: engines; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.engines (
    id bigint NOT NULL,
    name character varying(6) NOT NULL
);


ALTER TABLE public.engines OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 33175)
-- Name: engines_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.engines_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.engines_id_seq OWNER TO postgres;

--
-- TOC entry 3391 (class 0 OID 0)
-- Dependencies: 214
-- Name: engines_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.engines_id_seq OWNED BY public.engines.id;


--
-- TOC entry 217 (class 1259 OID 33183)
-- Name: maps; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.maps (
    id bigint NOT NULL,
    engine integer NOT NULL,
    game character varying(10) NOT NULL,
    name character varying(255) NOT NULL,
    gamemode character varying(5)
);


ALTER TABLE public.maps OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 33182)
-- Name: maps_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.maps_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.maps_id_seq OWNER TO postgres;

--
-- TOC entry 3392 (class 0 OID 0)
-- Dependencies: 216
-- Name: maps_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.maps_id_seq OWNED BY public.maps.id;


--
-- TOC entry 221 (class 1259 OID 33213)
-- Name: modules; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.modules (
    id bigint NOT NULL,
    engine integer NOT NULL,
    game character varying(10) NOT NULL,
    platform character varying(3) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.modules OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 33212)
-- Name: modules_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.modules_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.modules_id_seq OWNER TO postgres;

--
-- TOC entry 3393 (class 0 OID 0)
-- Dependencies: 220
-- Name: modules_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.modules_id_seq OWNED BY public.modules.id;


--
-- TOC entry 219 (class 1259 OID 33197)
-- Name: plugins; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.plugins (
    id bigint NOT NULL,
    engine integer NOT NULL,
    game character varying(10),
    uploader character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE public.plugins OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 33196)
-- Name: plugins_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.plugins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.plugins_id_seq OWNER TO postgres;

--
-- TOC entry 3394 (class 0 OID 0)
-- Dependencies: 218
-- Name: plugins_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.plugins_id_seq OWNED BY public.plugins.id;


--
-- TOC entry 223 (class 1259 OID 33227)
-- Name: uploaders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.uploaders (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    token character varying(255) NOT NULL
);


ALTER TABLE public.uploaders OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 33226)
-- Name: uploaders_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.uploaders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.uploaders_id_seq OWNER TO postgres;

--
-- TOC entry 3395 (class 0 OID 0)
-- Dependencies: 222
-- Name: uploaders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.uploaders_id_seq OWNED BY public.uploaders.id;


--
-- TOC entry 3203 (class 2604 OID 33243)
-- Name: bundles id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bundles ALTER COLUMN id SET DEFAULT nextval('public.bundles_id_seq'::regclass);


--
-- TOC entry 3198 (class 2604 OID 33179)
-- Name: engines id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.engines ALTER COLUMN id SET DEFAULT nextval('public.engines_id_seq'::regclass);


--
-- TOC entry 3199 (class 2604 OID 33186)
-- Name: maps id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maps ALTER COLUMN id SET DEFAULT nextval('public.maps_id_seq'::regclass);


--
-- TOC entry 3201 (class 2604 OID 33216)
-- Name: modules id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.modules ALTER COLUMN id SET DEFAULT nextval('public.modules_id_seq'::regclass);


--
-- TOC entry 3200 (class 2604 OID 33200)
-- Name: plugins id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.plugins ALTER COLUMN id SET DEFAULT nextval('public.plugins_id_seq'::regclass);


--
-- TOC entry 3202 (class 2604 OID 33230)
-- Name: uploaders id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uploaders ALTER COLUMN id SET DEFAULT nextval('public.uploaders_id_seq'::regclass);


--
-- TOC entry 3384 (class 0 OID 33240)
-- Dependencies: 225
-- Data for Name: bundles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bundles (id, name, plugins) FROM stdin;
\.


--
-- TOC entry 3374 (class 0 OID 33176)
-- Dependencies: 215
-- Data for Name: engines; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.engines (id, name) FROM stdin;
1	gold
2	source
\.


--
-- TOC entry 3376 (class 0 OID 33183)
-- Dependencies: 217
-- Data for Name: maps; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.maps (id, engine, game, name, gamemode) FROM stdin;
\.


--
-- TOC entry 3380 (class 0 OID 33213)
-- Dependencies: 221
-- Data for Name: modules; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.modules (id, engine, game, platform, name) FROM stdin;
\.


--
-- TOC entry 3378 (class 0 OID 33197)
-- Dependencies: 219
-- Data for Name: plugins; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.plugins (id, engine, game, uploader, name) FROM stdin;
\.


--
-- TOC entry 3382 (class 0 OID 33227)
-- Dependencies: 223
-- Data for Name: uploaders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.uploaders (id, name, token) FROM stdin;
1	Glaster	V5dFKftlGLTmbA4XLgNZyGJ1Ftyx9CIY5TQvlVT5vk83BnQEy8pxSVLrQiIjhMZU0CSo291hY0kBly1ILMioUhmcHMKojbPLqhoJnageADBpfcwxbr1k0RhypKHb4HSNihiQgHVVy8C3dc1RhW0zeWcGkmv2oeqsJI1FCiVukGTCgANguVfZknaM9M2vIxkVliIhltoyeq3cAs98ksyxkIiWB0xUWUsUYumdOkYFSUNtXantmciT3WxNR9aaz
2	Ufame	kzlfTdnOHWiJvhUgf8iBvid2noPtvPQDC37pRwevrXoP9FrWaUbDAXfPpe5nY2URhJQnBK59exDKIgGatjPwJTifhal4Qg0XRJ28WfjUbc6GLbu4JnS9feskbV8fF7FGk8EmVaByGiDOMPx16whRHxNKQoiQ3K84lQdZICd2QjvOAoOcFSJRUTWSNRhhfdua2YdwhXEew1kBofwWWe3jIhxrn2f2jqE73QWKPrtw3kQ1VNoRiminCoKqt4Vf7
\.


--
-- TOC entry 3396 (class 0 OID 0)
-- Dependencies: 224
-- Name: bundles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.bundles_id_seq', 1, false);


--
-- TOC entry 3397 (class 0 OID 0)
-- Dependencies: 214
-- Name: engines_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.engines_id_seq', 2, true);


--
-- TOC entry 3398 (class 0 OID 0)
-- Dependencies: 216
-- Name: maps_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.maps_id_seq', 1, false);


--
-- TOC entry 3399 (class 0 OID 0)
-- Dependencies: 220
-- Name: modules_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.modules_id_seq', 1, false);


--
-- TOC entry 3400 (class 0 OID 0)
-- Dependencies: 218
-- Name: plugins_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.plugins_id_seq', 1, false);


--
-- TOC entry 3401 (class 0 OID 0)
-- Dependencies: 222
-- Name: uploaders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.uploaders_id_seq', 2, true);


--
-- TOC entry 3225 (class 2606 OID 33249)
-- Name: bundles bundles_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bundles
    ADD CONSTRAINT bundles_name_key UNIQUE (name);


--
-- TOC entry 3227 (class 2606 OID 33247)
-- Name: bundles bundles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bundles
    ADD CONSTRAINT bundles_pkey PRIMARY KEY (id);


--
-- TOC entry 3205 (class 2606 OID 33181)
-- Name: engines engines_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.engines
    ADD CONSTRAINT engines_pkey PRIMARY KEY (id);


--
-- TOC entry 3207 (class 2606 OID 33190)
-- Name: maps maps_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maps
    ADD CONSTRAINT maps_name_key UNIQUE (name);


--
-- TOC entry 3209 (class 2606 OID 33188)
-- Name: maps maps_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maps
    ADD CONSTRAINT maps_pkey PRIMARY KEY (id);


--
-- TOC entry 3215 (class 2606 OID 33220)
-- Name: modules modules_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.modules
    ADD CONSTRAINT modules_name_key UNIQUE (name);


--
-- TOC entry 3217 (class 2606 OID 33218)
-- Name: modules modules_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.modules
    ADD CONSTRAINT modules_pkey PRIMARY KEY (id);


--
-- TOC entry 3211 (class 2606 OID 33206)
-- Name: plugins plugins_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.plugins
    ADD CONSTRAINT plugins_name_key UNIQUE (name);


--
-- TOC entry 3213 (class 2606 OID 33204)
-- Name: plugins plugins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.plugins
    ADD CONSTRAINT plugins_pkey PRIMARY KEY (id);


--
-- TOC entry 3219 (class 2606 OID 33236)
-- Name: uploaders uploaders_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uploaders
    ADD CONSTRAINT uploaders_name_key UNIQUE (name);


--
-- TOC entry 3221 (class 2606 OID 33234)
-- Name: uploaders uploaders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uploaders
    ADD CONSTRAINT uploaders_pkey PRIMARY KEY (id);


--
-- TOC entry 3223 (class 2606 OID 33238)
-- Name: uploaders uploaders_token_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uploaders
    ADD CONSTRAINT uploaders_token_key UNIQUE (token);


--
-- TOC entry 3228 (class 2606 OID 33191)
-- Name: maps maps_engine_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maps
    ADD CONSTRAINT maps_engine_fkey FOREIGN KEY (engine) REFERENCES public.engines(id);


--
-- TOC entry 3230 (class 2606 OID 33221)
-- Name: modules modules_engine_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.modules
    ADD CONSTRAINT modules_engine_fkey FOREIGN KEY (engine) REFERENCES public.engines(id);


--
-- TOC entry 3229 (class 2606 OID 33207)
-- Name: plugins plugins_engine_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.plugins
    ADD CONSTRAINT plugins_engine_fkey FOREIGN KEY (engine) REFERENCES public.engines(id);


-- Completed on 2023-06-10 16:38:07

--
-- PostgreSQL database dump complete
--

