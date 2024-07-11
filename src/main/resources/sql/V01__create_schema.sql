--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

-- Started on 2024-06-03 10:46:08

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

--
-- TOC entry 19 (class 2615 OID 84949)
-- Name: sup_leg; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA sup_leg;


ALTER SCHEMA sup_leg OWNER TO postgres;

--
-- TOC entry 537 (class 1259 OID 84950)
-- Name: abertura_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.abertura_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.abertura_id_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 538 (class 1259 OID 84951)
-- Name: abertura; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.abertura (
    id bigint DEFAULT nextval('sup_leg.abertura_id_seq'::regclass) NOT NULL,
    nome character varying(255) NOT NULL,
    status boolean NOT NULL
);


ALTER TABLE sup_leg.abertura OWNER TO postgres;

--
-- TOC entry 539 (class 1259 OID 84955)
-- Name: documento_acessorio_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.documento_acessorio_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.documento_acessorio_id_seq OWNER TO postgres;

--
-- TOC entry 540 (class 1259 OID 84956)
-- Name: documento_acessorio; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.documento_acessorio (
    id bigint DEFAULT nextval('sup_leg.documento_acessorio_id_seq'::regclass) NOT NULL,
    id_ordem bigint,
    roteiro_secretario bigint
);


ALTER TABLE sup_leg.documento_acessorio OWNER TO postgres;

--
-- TOC entry 541 (class 1259 OID 84960)
-- Name: fechamento_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.fechamento_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.fechamento_id_seq OWNER TO postgres;

--
-- TOC entry 542 (class 1259 OID 84961)
-- Name: fechamento; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.fechamento (
    id bigint DEFAULT nextval('sup_leg.fechamento_id_seq'::regclass) NOT NULL,
    nome character varying(255) NOT NULL,
    status boolean NOT NULL
);


ALTER TABLE sup_leg.fechamento OWNER TO postgres;

--
-- TOC entry 543 (class 1259 OID 84965)
-- Name: link_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.link_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.link_id_seq OWNER TO postgres;

--
-- TOC entry 544 (class 1259 OID 84966)
-- Name: link; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.link (
    id bigint DEFAULT nextval('sup_leg.link_id_seq'::regclass) NOT NULL,
    nome character varying(255) NOT NULL,
    parecer boolean NOT NULL,
    url character varying(255) NOT NULL,
    documento_acessorio_id bigint
);


ALTER TABLE sup_leg.link OWNER TO postgres;

--
-- TOC entry 545 (class 1259 OID 84972)
-- Name: log_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.log_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.log_id_seq OWNER TO postgres;

--
-- TOC entry 546 (class 1259 OID 84973)
-- Name: log; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.log (
    id bigint DEFAULT nextval('sup_leg.log_id_seq'::regclass) NOT NULL,
    data_acao timestamp without time zone NOT NULL,
    estado_final text,
    estado_inicial text,
    mensagem text NOT NULL,
    tipo_acao character varying(255) NOT NULL,
    usuario_id bigint NOT NULL,
    ip character varying(255)
);


ALTER TABLE sup_leg.log OWNER TO postgres;

--
-- TOC entry 547 (class 1259 OID 84979)
-- Name: mesa_modelo_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.mesa_modelo_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.mesa_modelo_id_seq OWNER TO postgres;

--
-- TOC entry 548 (class 1259 OID 84980)
-- Name: mesa_modelo; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.mesa_modelo (
    id bigint DEFAULT nextval('sup_leg.mesa_modelo_id_seq'::regclass) NOT NULL,
    status boolean,
    corregedor bigint NOT NULL,
    presidente bigint NOT NULL,
    secretario_1 bigint NOT NULL,
    secretario_2 bigint NOT NULL,
    secretario_3 bigint NOT NULL,
    secretario_4 bigint NOT NULL,
    vice_presidente_1 bigint NOT NULL,
    vice_presidente_2 bigint NOT NULL,
    vice_presidente_3 bigint NOT NULL
);


ALTER TABLE sup_leg.mesa_modelo OWNER TO postgres;

--
-- TOC entry 549 (class 1259 OID 84984)
-- Name: modelo_roteiro_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.modelo_roteiro_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.modelo_roteiro_id_seq OWNER TO postgres;

--
-- TOC entry 550 (class 1259 OID 84985)
-- Name: modelo_roteiro; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.modelo_roteiro (
    id bigint DEFAULT nextval('sup_leg.modelo_roteiro_id_seq'::regclass) NOT NULL,
    nome character varying(255) NOT NULL,
    possui_ordem_dia character varying(255) NOT NULL,
    status boolean NOT NULL,
    abertura_id bigint,
    fechamento_id bigint
);


ALTER TABLE sup_leg.modelo_roteiro OWNER TO postgres;

--
-- TOC entry 551 (class 1259 OID 84991)
-- Name: ordem_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.ordem_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.ordem_id_seq OWNER TO postgres;

--
-- TOC entry 552 (class 1259 OID 84992)
-- Name: ordem; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.ordem (
    id bigint DEFAULT nextval('sup_leg.ordem_id_seq'::regclass) NOT NULL,
    autor character varying(255) NOT NULL,
    autor_materia_principal character varying(255),
    ementa text NOT NULL,
    id_materia character varying(255),
    id_materia_principal character varying(255),
    materia character varying(255) NOT NULL,
    materia_materia_principal character varying(255),
    ordem integer NOT NULL,
    processo character varying(255),
    protocolo character varying(255),
    resultado character varying(255),
    tipo_autor character varying(255),
    tipo_autor_materia_principal character varying(255),
    turno character varying(255),
    turno_votacao character varying(255) NOT NULL,
    veto boolean DEFAULT false,
    parecer_procuradoria bigint,
    roteiro_materia bigint,
    status_materia bigint,
    status_tramitacao bigint,
    ordem_dia bigint,
    etiqueta character varying(255),
    cor_etiqueta character varying(255)
);


ALTER TABLE sup_leg.ordem OWNER TO postgres;

--
-- TOC entry 553 (class 1259 OID 84999)
-- Name: ordem_do_dia_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.ordem_do_dia_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.ordem_do_dia_id_seq OWNER TO postgres;

--
-- TOC entry 554 (class 1259 OID 85000)
-- Name: ordem_do_dia; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.ordem_do_dia (
    id bigint DEFAULT nextval('sup_leg.ordem_do_dia_id_seq'::regclass) NOT NULL,
    data_sessao timestamp without time zone NOT NULL
);


ALTER TABLE sup_leg.ordem_do_dia OWNER TO postgres;

--
-- TOC entry 555 (class 1259 OID 85004)
-- Name: parecer_procuradoria_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.parecer_procuradoria_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.parecer_procuradoria_id_seq OWNER TO postgres;

--
-- TOC entry 556 (class 1259 OID 85005)
-- Name: parecer_procuradoria; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.parecer_procuradoria (
    id bigint DEFAULT nextval('sup_leg.parecer_procuradoria_id_seq'::regclass) NOT NULL,
    nome character varying(255) NOT NULL,
    status boolean NOT NULL,
    background character varying(255)
);


ALTER TABLE sup_leg.parecer_procuradoria OWNER TO postgres;

--
-- TOC entry 557 (class 1259 OID 85011)
-- Name: parlamentar_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.parlamentar_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.parlamentar_id_seq OWNER TO postgres;

--
-- TOC entry 558 (class 1259 OID 85012)
-- Name: parlamentar; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.parlamentar (
    id bigint DEFAULT nextval('sup_leg.parlamentar_id_seq'::regclass) NOT NULL,
    nome character varying(255) NOT NULL,
    status boolean NOT NULL
);


ALTER TABLE sup_leg.parlamentar OWNER TO postgres;

--
-- TOC entry 559 (class 1259 OID 85016)
-- Name: roteiro_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.roteiro_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.roteiro_id_seq OWNER TO postgres;

--
-- TOC entry 560 (class 1259 OID 85017)
-- Name: roteiro; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.roteiro (
    id bigint DEFAULT nextval('sup_leg.roteiro_id_seq'::regclass) NOT NULL,
    data_sessao timestamp without time zone NOT NULL,
    nome character varying(255) NOT NULL,
    id_modelo_roteiro bigint,
    id_ordem_dia bigint
);


ALTER TABLE sup_leg.roteiro OWNER TO postgres;

--
-- TOC entry 561 (class 1259 OID 85021)
-- Name: roteiro_materia_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.roteiro_materia_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.roteiro_materia_id_seq OWNER TO postgres;

--
-- TOC entry 562 (class 1259 OID 85022)
-- Name: roteiro_materia; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.roteiro_materia (
    id bigint DEFAULT nextval('sup_leg.roteiro_materia_id_seq'::regclass) NOT NULL,
    status boolean NOT NULL,
    texto text NOT NULL,
    tipo_materia bigint NOT NULL
);


ALTER TABLE sup_leg.roteiro_materia OWNER TO postgres;

--
-- TOC entry 563 (class 1259 OID 85028)
-- Name: roteiro_secretario_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.roteiro_secretario_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.roteiro_secretario_id_seq OWNER TO postgres;

--
-- TOC entry 564 (class 1259 OID 85029)
-- Name: roteiro_secretario; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.roteiro_secretario (
    id bigint DEFAULT nextval('sup_leg.roteiro_secretario_id_seq'::regclass) NOT NULL,
    id_roteiro bigint
);


ALTER TABLE sup_leg.roteiro_secretario OWNER TO postgres;

--
-- TOC entry 565 (class 1259 OID 85033)
-- Name: sessao_legislativa_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.sessao_legislativa_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.sessao_legislativa_id_seq OWNER TO postgres;

--
-- TOC entry 566 (class 1259 OID 85034)
-- Name: sessao_legislativa; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.sessao_legislativa (
    id bigint DEFAULT nextval('sup_leg.sessao_legislativa_id_seq'::regclass) NOT NULL,
    legislatura integer NOT NULL,
    sessao_legislativa integer NOT NULL,
    status boolean NOT NULL
);


ALTER TABLE sup_leg.sessao_legislativa OWNER TO postgres;

--
-- TOC entry 567 (class 1259 OID 85038)
-- Name: status_materia_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.status_materia_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.status_materia_id_seq OWNER TO postgres;

--
-- TOC entry 568 (class 1259 OID 85039)
-- Name: status_materia; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.status_materia (
    id bigint DEFAULT nextval('sup_leg.status_materia_id_seq'::regclass) NOT NULL,
    nome character varying(255) NOT NULL,
    status boolean NOT NULL,
    background character varying(255)
);


ALTER TABLE sup_leg.status_materia OWNER TO postgres;

--
-- TOC entry 569 (class 1259 OID 85045)
-- Name: status_tramitacao_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.status_tramitacao_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.status_tramitacao_id_seq OWNER TO postgres;

--
-- TOC entry 570 (class 1259 OID 85046)
-- Name: status_tramitacao; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.status_tramitacao (
    id bigint DEFAULT nextval('sup_leg.status_tramitacao_id_seq'::regclass) NOT NULL,
    nome character varying(255) NOT NULL,
    status boolean NOT NULL,
    background character varying(255)
);


ALTER TABLE sup_leg.status_tramitacao OWNER TO postgres;

--
-- TOC entry 571 (class 1259 OID 85052)
-- Name: tipo_autor_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.tipo_autor_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.tipo_autor_id_seq OWNER TO postgres;

--
-- TOC entry 572 (class 1259 OID 85053)
-- Name: tipo_autor; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.tipo_autor (
    id bigint DEFAULT nextval('sup_leg.tipo_autor_id_seq'::regclass) NOT NULL,
    nome character varying(255) NOT NULL,
    status boolean NOT NULL
);


ALTER TABLE sup_leg.tipo_autor OWNER TO postgres;

--
-- TOC entry 573 (class 1259 OID 85057)
-- Name: tipo_materia_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.tipo_materia_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.tipo_materia_id_seq OWNER TO postgres;

--
-- TOC entry 574 (class 1259 OID 85058)
-- Name: tipo_materia; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.tipo_materia (
    id bigint DEFAULT nextval('sup_leg.tipo_materia_id_seq'::regclass) NOT NULL,
    nome character varying(255) NOT NULL,
    status boolean NOT NULL
);


ALTER TABLE sup_leg.tipo_materia OWNER TO postgres;

--
-- TOC entry 575 (class 1259 OID 85062)
-- Name: trecho_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.trecho_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.trecho_id_seq OWNER TO postgres;

--
-- TOC entry 576 (class 1259 OID 85063)
-- Name: trecho; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.trecho (
    id bigint DEFAULT nextval('sup_leg.trecho_id_seq'::regclass) NOT NULL,
    texto text NOT NULL,
    abertura_id bigint,
    fechamento_id bigint,
    cor_de_fundo text
);


ALTER TABLE sup_leg.trecho OWNER TO postgres;

--
-- TOC entry 577 (class 1259 OID 85069)
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: sup_leg; Owner: postgres
--

CREATE SEQUENCE sup_leg.usuario_id_seq
    START WITH 200
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sup_leg.usuario_id_seq OWNER TO postgres;

--
-- TOC entry 578 (class 1259 OID 85070)
-- Name: usuario; Type: TABLE; Schema: sup_leg; Owner: postgres
--

CREATE TABLE sup_leg.usuario (
    id bigint DEFAULT nextval('sup_leg.usuario_id_seq'::regclass) NOT NULL,
    login character varying(20) NOT NULL,
    matricula character varying(10) NOT NULL,
    nome character varying(40) NOT NULL,
    senha character varying(255) NOT NULL,
    status boolean NOT NULL,
    tipo character varying(255) NOT NULL
);


ALTER TABLE sup_leg.usuario OWNER TO postgres;

--
-- TOC entry 4149 (class 0 OID 84951)
-- Dependencies: 538
-- Data for Name: abertura; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.abertura VALUES (2, 'ABERTURA PADRÃO', true);


--
-- TOC entry 4151 (class 0 OID 84956)
-- Dependencies: 540
-- Data for Name: documento_acessorio; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--



--
-- TOC entry 4153 (class 0 OID 84961)
-- Dependencies: 542
-- Data for Name: fechamento; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.fechamento VALUES (1, 'FECHAMENTO PADRÃO', true);


--
-- TOC entry 4155 (class 0 OID 84966)
-- Dependencies: 544
-- Data for Name: link; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--



--
-- TOC entry 4157 (class 0 OID 84973)
-- Dependencies: 546
-- Data for Name: log; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.log VALUES (200, '2024-06-03 10:16:45.875448', '', '', 'O usuário entrou no sistema.', 'ENTROU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (201, '2024-06-03 10:16:46.48241', '', '', 'O usuário acessou a página: /supleg/pages/common/roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (202, '2024-06-03 10:16:49.858958', '', '', 'O usuário acessou a página: /supleg/pages/user/sessaoLegislativa.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (203, '2024-06-03 10:16:53.744891', '', '', 'O usuário acessou a página: /supleg/pages/user/modelo-roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (204, '2024-06-03 10:17:03.06358', 'ModeloRoteiro(id=200, nome=ROTEIRO PADRÃO, possuiOrdemDia=ORDEM_AUTOMATICA, status=true)', '', 'O usuário salvou o Modelo Roteiro.', 'SALVOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (205, '2024-06-03 10:17:03.207587', '', '', 'O usuário acessou a página: /supleg/pages/user/modelo-roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (206, '2024-06-03 10:17:08.669453', '', '', 'O usuário acessou a página: /supleg/pages/common/roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (207, '2024-06-03 10:17:44.046335', 'Roteiro(id=200, dataSessao=2024-05-28T00:00, nome=23ª Sessão Ordinária de 2024)', '', 'O usuário salvou o roteiro', 'SALVOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (208, '2024-06-03 10:17:44.232246', '', '', 'O usuário acessou a página: /supleg/pages/common/roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (209, '2024-06-03 10:18:32.826113', '', '', 'O usuário acessou a página: /supleg/pages/common/roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (210, '2024-06-03 10:18:49.492102', 'Noda data: 2024-06-04', 'ID roteiro: 200', 'O usuario alterou a data do roteiro', 'ATUALIZOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (211, '2024-06-03 10:18:50.735797', '', '', 'O usuário acessou a página: /supleg/pages/common/roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (212, '2024-06-03 10:19:21.092821', '', '', 'O usuário saiu do sistema.', 'SAIU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (213, '2024-06-03 10:20:51.510311', '', '', 'O usuário entrou no sistema.', 'ENTROU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (214, '2024-06-03 10:20:51.64731', '', '', 'O usuário acessou a página: /supleg/pages/common/roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (215, '2024-06-03 10:21:03.873523', 'Roteiro(id=null, dataSessao=null, nome=10ª Sessão Ordinária de 2024)', '', 'O usuário salvou o roteiro', 'SALVOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (216, '2024-06-03 10:21:04.02353', '', '', 'O usuário acessou a página: /supleg/pages/common/roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (217, '2024-06-03 10:21:18.909715', '', '', 'O usuário acessou a página: /supleg/pages/user/sessaoLegislativa.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (218, '2024-06-03 10:21:30.407607', '', '', 'O usuário acessou a página: /supleg/', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (219, '2024-06-03 10:21:34.518597', '', '', 'O usuário acessou a página: /supleg/exibir-roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (220, '2024-06-03 10:21:42.975501', '', '', 'O usuário acessou a página: /supleg/pages/user/parlamentar.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (221, '2024-06-03 10:21:49.053218', '', '', 'O usuário acessou a página: /supleg/pages/user/modelo-mesa.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (222, '2024-06-03 10:21:59.914228', '', '', 'O usuário acessou a página: /supleg/pages/common/roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (223, '2024-06-03 10:22:04.20403', '', '', 'O usuário acessou a página: /supleg/pages/common/exibir-roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (224, '2024-06-03 10:28:43.137409', '', '', 'O usuário acessou a página: /supleg/pages/common/roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (225, '2024-06-03 10:28:50.504572', '', 'ID Roteiro: 200', 'O usuário apagou o roteiro', 'APAGOU', 1, '127.0.0.1');
INSERT INTO sup_leg.log VALUES (226, '2024-06-03 10:28:50.677571', '', '', 'O usuário acessou a página: /supleg/pages/common/roteiro.xhtml', 'ACESSOU', 1, '127.0.0.1');


--
-- TOC entry 4159 (class 0 OID 84980)
-- Dependencies: 548
-- Data for Name: mesa_modelo; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.mesa_modelo VALUES (5, true, 11, 3, 10, 7, 8, 9, 4, 5, 6);


--
-- TOC entry 4161 (class 0 OID 84985)
-- Dependencies: 550
-- Data for Name: modelo_roteiro; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.modelo_roteiro VALUES (200, 'ROTEIRO PADRÃO', 'ORDEM_AUTOMATICA', true, 2, 1);


--
-- TOC entry 4163 (class 0 OID 84992)
-- Dependencies: 552
-- Data for Name: ordem; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--



--
-- TOC entry 4165 (class 0 OID 85000)
-- Dependencies: 554
-- Data for Name: ordem_do_dia; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--



--
-- TOC entry 4167 (class 0 OID 85005)
-- Dependencies: 556
-- Data for Name: parecer_procuradoria; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.parecer_procuradoria VALUES (1, 'Pela Constitucionalidade', true, '#c6dbe1ff');
INSERT INTO sup_leg.parecer_procuradoria VALUES (2, 'Sem Parecer', true, '#e8eaedff');
INSERT INTO sup_leg.parecer_procuradoria VALUES (3, 'Pela Inconstitucionalidade', true, '#e8eaedff');
INSERT INTO sup_leg.parecer_procuradoria VALUES (4, 'Pela Inconstitucionalidade Parcial', true, '#127efae5');
INSERT INTO sup_leg.parecer_procuradoria VALUES (5, 'Pela Manutenção do Veto', true, '#3998b5ff');
INSERT INTO sup_leg.parecer_procuradoria VALUES (6, 'Pela Constitucionalidade Parcial', true, '#c6dbe1ff');
INSERT INTO sup_leg.parecer_procuradoria VALUES (7, 'Pela Rejeição do Veto', true, '#e8eaedff');
INSERT INTO sup_leg.parecer_procuradoria VALUES (8, 'Pela Manutenção do Veto Total', true, '#3998b5ff');
INSERT INTO sup_leg.parecer_procuradoria VALUES (10, 'Pela Manutenção do Veto Parcial', true, '#ffe5a0ff');
INSERT INTO sup_leg.parecer_procuradoria VALUES (11, '-', true, NULL);


--
-- TOC entry 4169 (class 0 OID 85012)
-- Dependencies: 558
-- Data for Name: parlamentar; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.parlamentar VALUES (3, 'SOLDADO SAMPAIO', true);
INSERT INTO sup_leg.parlamentar VALUES (4, 'MARCELO CABRAL', true);
INSERT INTO sup_leg.parlamentar VALUES (5, 'CHICO MOZART', true);
INSERT INTO sup_leg.parlamentar VALUES (6, 'EDER LOURINHO', true);
INSERT INTO sup_leg.parlamentar VALUES (7, 'AURELINA MEDEIROS', true);
INSERT INTO sup_leg.parlamentar VALUES (8, 'RÁRISSON BARBOSA', true);
INSERT INTO sup_leg.parlamentar VALUES (9, 'ODILON FILHO', true);
INSERT INTO sup_leg.parlamentar VALUES (10, 'JORGE EVERTON', true);
INSERT INTO sup_leg.parlamentar VALUES (11, 'RENATO SILVA', true);


--
-- TOC entry 4171 (class 0 OID 85017)
-- Dependencies: 560
-- Data for Name: roteiro; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--



--
-- TOC entry 4173 (class 0 OID 85022)
-- Dependencies: 562
-- Data for Name: roteiro_materia; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.roteiro_materia VALUES (2, true, '<ul><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DO PARECER.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM DISCUSSÃO A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">A VOTAÇÃO SERÁ NOMINAL/ELETRÔNICA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">SIM</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, OS DEPUTADOS APROVAM A MATÉRIA, VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">NÃO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, REJEITAM A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A ABERTURA DO PAINEL ELETRÔNICO PARA VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">ENCERRADA A VOTAÇÃO.</span></li><li><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">DECLARO</span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent"> </strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">APROVADO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">/</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">REJEITADO</strong><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">O</span><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><strong style="background-color:transparent">{NOME-MATERIA}</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">.</strong></li></ul>', 1);
INSERT INTO sup_leg.roteiro_materia VALUES (3, true, '<ul><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DO PARECER.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">EM DISCUSSÃO A MATÉRIA.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">EM VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">A VOTAÇÃO SERÁ NOMINAL/ELETRÔNICA.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">VOTANDO </span><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">“</strong><strong style="background-color:transparent;color:rgb( 0 , 255 , 0 )">SIM</strong><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">”</strong><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">, OS DEPUTADOS APROVAM A MATÉRIA, VOTANDO </span><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">“</strong><strong style="background-color:transparent;color:rgb( 204 , 0 , 0 )">NÃO</strong><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">”</strong><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">, REJEITAM A MATÉRIA.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">SOLICITO A ABERTURA DO PAINEL ELETRÔNICO PARA VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">ENCERRADA A VOTAÇÃO.</span></li><li><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">DECLARO</span><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )"> </strong><strong style="background-color:transparent;color:rgb( 0 , 255 , 0 )">APROVADO</strong><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">/</strong><strong style="background-color:transparent;color:rgb( 204 , 0 , 0 )">REJEITADO</strong><span style="background-color:transparent;color:rgb( 204 , 0 , 0 )"> </span><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">O</span><span style="background-color:transparent;color:rgb( 204 , 0 , 0 )"> </span><strong style="background-color:transparent">{NOME-MATERIA}.</strong></li></ul>', 2);
INSERT INTO sup_leg.roteiro_materia VALUES (4, true, '<ul><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DO PEDIDO DE INFORMAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM DISCUSSÃO A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">A VOTAÇÃO SERÁ NOMINAL/ELETRÔNICA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">SIM</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, OS DEPUTADOS APROVAM A MATÉRIA, VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">NÃO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, REJEITAM A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A ABERTURA DO PAINEL ELETRÔNICO PARA VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">ENCERRADA A VOTAÇÃO.</span></li><li><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">DECLARO</span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent"> </strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">APROVADO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">/</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">REJEITADO</strong><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">O</span><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><strong style="background-color:transparent">{NOME-MATERIA}</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">.</strong></li></ul>', 8);
INSERT INTO sup_leg.roteiro_materia VALUES (5, true, '<ul><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DA MOÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM DISCUSSÃO A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM VOTAÇÃO.</span></li><li class="ql-align-justify"><strong style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">A VOTAÇÃO SERÁ NOMINAL/ELETRÔNICA.</strong></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">“</strong><strong style="color:rgb( 0 , 255 , 0 );background-color:rgb( 255 , 255 , 0 )">SIM</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">, OS DEPUTADOS APROVAM A MATÉRIA, VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">“</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">NÃO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">, REJEITAM A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">SOLICITO A ABERTURA DO PAINEL ELETRÔNICO PARA VOTAÇÃO.</span></li><li class="ql-align-justify"><strong style="color:rgb( 0 , 0 , 0 );background-color:rgb( 0 , 255 , 255 )">A VOTAÇÃO SERÁ SIMBÓLICA.</strong></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:rgb( 0 , 255 , 255 )">OS DEPUTADOS QUE CONCORDAM PERMANEÇAM COMO ESTÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">ENCERRADA A VOTAÇÃO.</span></li><li><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">DECLARO</span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent"> </strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">APROVADA</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">/</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">REJEITADA</strong><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">A</span><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">{NOME-MATERIA}.</strong></li></ul>', 3);
INSERT INTO sup_leg.roteiro_materia VALUES (6, true, '<ul><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DO PARECER.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM DISCUSSÃO A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">A VOTAÇÃO SERÁ NOMINAL/ELETRÔNICA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">SIM</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, OS DEPUTADOS APROVAM A MATÉRIA, VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">NÃO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, REJEITAM A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A ABERTURA DO PAINEL ELETRÔNICO PARA VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">ENCERRADA A VOTAÇÃO.</span></li><li><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">DECLARO</span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent"> </strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">APROVADO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">/</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">REJEITADO</strong><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">O</span><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><strong style="background-color:transparent">{NOME-MATERIA}</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">.</strong></li></ul><p><br /></p>', 10);
INSERT INTO sup_leg.roteiro_materia VALUES (7, true, '<ul><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DO PARECER.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM DISCUSSÃO A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">A VOTAÇÃO SERÁ NOMINAL/ELETRÔNICA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">SIM</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, OS DEPUTADOS MANTÉM O VETO, VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">NÃO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, REJEITAM O VETO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A ABERTURA DO PAINEL ELETRÔNICO PARA VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">ENCERRADA A VOTAÇÃO.</span></li><li><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">DECLARO</span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent"> </strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">APROVADO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">/</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">REJEITADO</strong><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">A</span><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><strong style="background-color:transparent">{NOME-MATERIA}</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">.</strong></li></ul>', 12);
INSERT INTO sup_leg.roteiro_materia VALUES (8, true, '<ul><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DO PARECER.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">EM DISCUSSÃO A MATÉRIA.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">EM VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">A VOTAÇÃO SERÁ NOMINAL/ELETRÔNICA.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">VOTANDO </span><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">“</strong><strong style="background-color:transparent;color:rgb( 0 , 255 , 0 )">SIM</strong><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">”</strong><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">, OS DEPUTADOS APROVAM A MATÉRIA, VOTANDO </span><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">“</strong><strong style="background-color:transparent;color:rgb( 204 , 0 , 0 )">NÃO</strong><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">”</strong><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">, REJEITAM A MATÉRIA.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">SOLICITO A ABERTURA DO PAINEL ELETRÔNICO PARA VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">ENCERRADA A VOTAÇÃO.</span></li><li><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">DECLARO</span><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )"> </strong><strong style="background-color:transparent;color:rgb( 0 , 255 , 0 )">APROVADO</strong><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">/</strong><strong style="background-color:transparent;color:rgb( 204 , 0 , 0 )">REJEITADO</strong><span style="background-color:transparent;color:rgb( 204 , 0 , 0 )"> </span><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">O</span><span style="background-color:transparent;color:rgb( 204 , 0 , 0 )"> </span><strong style="background-color:transparent">{NOME-MATERIA}</strong><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">.</strong></li></ul>', 13);
INSERT INTO sup_leg.roteiro_materia VALUES (9, true, '<ul><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DO PARECER.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM DISCUSSÃO A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">A VOTAÇÃO SERÁ NOMINAL/ELETRÔNICA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">SIM</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, OS DEPUTADOS APROVAM A MATÉRIA, VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">NÃO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, REJEITAM A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A ABERTURA DO PAINEL ELETRÔNICO PARA VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">ENCERRADA A VOTAÇÃO.</span></li><li><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">DECLARO</span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent"> </strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">APROVADO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">/</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">REJEITADO</strong><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">O</span><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><strong style="background-color:transparent">{NOME-MATERIA}</strong><span style="background-color:transparent">.</span></li></ul>', 14);
INSERT INTO sup_leg.roteiro_materia VALUES (10, true, '<ul><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DO PARECER.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM DISCUSSÃO A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">A VOTAÇÃO SERÁ NOMINAL/ELETRÔNICA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">SIM</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, OS DEPUTADOS APROVAM A MATÉRIA, VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">NÃO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, REJEITAM A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A ABERTURA DO PAINEL ELETRÔNICO PARA VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">ENCERRADA A VOTAÇÃO.</span></li><li><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">DECLARO</span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent"> </strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">APROVADO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">/</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">REJEITADO</strong><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">O</span><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><strong style="background-color:transparent">{NOME-MATERIA}</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">.</strong></li></ul>', 15);
INSERT INTO sup_leg.roteiro_materia VALUES (11, true, '<ul><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DO PARECER.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM DISCUSSÃO A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">A VOTAÇÃO SERÁ NOMINAL/ELETRÔNICA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">SIM</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, OS DEPUTADOS APROVAM A MATÉRIA, VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">“</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">NÃO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">, REJEITAM A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A ABERTURA DO PAINEL ELETRÔNICO PARA VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">ENCERRADA A VOTAÇÃO.</span></li><li><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">DECLARO</span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent"> </strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">APROVADO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">/</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">REJEITADO</strong><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">A</span><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><strong style="background-color:transparent">{NOME-MATERIA}</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">.</strong></li></ul>', 16);
INSERT INTO sup_leg.roteiro_materia VALUES (12, true, '<ul><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DO REQUERIMENTO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM DISCUSSÃO A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM VOTAÇÃO.</span></li><li class="ql-align-justify"><strong style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">A VOTAÇÃO SERÁ NOMINAL/ELETRÔNICA.</strong></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">“</strong><strong style="color:rgb( 0 , 255 , 0 );background-color:rgb( 255 , 255 , 0 )">SIM</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">, OS DEPUTADOS APROVAM A MATÉRIA, VOTANDO </span><strong style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">“</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">NÃO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">”</strong><span style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">, REJEITAM A MATÉRIA.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:rgb( 255 , 255 , 0 )">SOLICITO A ABERTURA DO PAINEL ELETRÔNICO PARA VOTAÇÃO.</span></li><li class="ql-align-justify"><strong style="color:rgb( 0 , 0 , 0 );background-color:rgb( 0 , 255 , 255 )">A VOTAÇÃO SERÁ SIMBÓLICA.</strong></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:rgb( 0 , 255 , 255 )">OS DEPUTADOS QUE CONCORDAM PERMANEÇAM COMO ESTÃO.</span></li><li class="ql-align-justify"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">ENCERRADA A VOTAÇÃO.</span></li><li><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">DECLARO</span><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent"> </strong><strong style="color:rgb( 0 , 255 , 0 );background-color:transparent">APROVADO</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">/</strong><strong style="color:rgb( 204 , 0 , 0 );background-color:transparent">REJEITADO</strong><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">O</span><span style="color:rgb( 204 , 0 , 0 );background-color:transparent"> </span><strong style="background-color:transparent">{NOME-MATERIA}</strong><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">.</strong></li></ul>', 11);
INSERT INTO sup_leg.roteiro_materia VALUES (13, true, '<ul><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DO PARECER.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">EM DISCUSSÃO A MATÉRIA.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">EM VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">A VOTAÇÃO SERÁ NOMINAL/ELETRÔNICA.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">VOTANDO </span><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">“</strong><strong style="background-color:transparent;color:rgb( 0 , 255 , 0 )">SIM</strong><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">”</strong><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">, OS DEPUTADOS MANTÉM O VETO, VOTANDO </span><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">“</strong><strong style="background-color:transparent;color:rgb( 204 , 0 , 0 )">NÃO</strong><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">”</strong><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">, REJEITAM O VETO.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">SOLICITO A ABERTURA DO PAINEL ELETRÔNICO PARA VOTAÇÃO.</span></li><li class="ql-align-justify"><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">ENCERRADA A VOTAÇÃO.</span></li><li><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">DECLARO</span><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )"> </strong><strong style="background-color:transparent;color:rgb( 0 , 255 , 0 )">APROVADO</strong><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">/</strong><strong style="background-color:transparent;color:rgb( 204 , 0 , 0 )">REJEITADO</strong><span style="background-color:transparent;color:rgb( 204 , 0 , 0 )"> </span><span style="background-color:transparent;color:rgb( 0 , 0 , 0 )">A</span><span style="background-color:transparent;color:rgb( 204 , 0 , 0 )"> </span><strong style="background-color:transparent;color:rgb( 0 , 0 , 0 )">{NOME-MATERIA}.</strong></li></ul>', 17);


--
-- TOC entry 4175 (class 0 OID 85029)
-- Dependencies: 564
-- Data for Name: roteiro_secretario; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--



--
-- TOC entry 4177 (class 0 OID 85034)
-- Dependencies: 566
-- Data for Name: sessao_legislativa; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.sessao_legislativa VALUES (1, 9, 2, true);


--
-- TOC entry 4179 (class 0 OID 85039)
-- Dependencies: 568
-- Data for Name: status_materia; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.status_materia VALUES (1, 'Em Andamento', true, '#e6cff2ff');
INSERT INTO sup_leg.status_materia VALUES (2, 'Não foi Votado', true, '#ffe5a0ff');
INSERT INTO sup_leg.status_materia VALUES (3, 'Aprovado', true, '#29c782ed');
INSERT INTO sup_leg.status_materia VALUES (4, 'Rejeitado', true, '#e31b1bbf');
INSERT INTO sup_leg.status_materia VALUES (5, 'Veto Mantido', true, '#e31b1bbf');
INSERT INTO sup_leg.status_materia VALUES (6, 'Retirado de Pauta', true, '#bfe1f6ff');
INSERT INTO sup_leg.status_materia VALUES (7, 'Veto Rejeitado', true, '#e31b1bbf');


--
-- TOC entry 4181 (class 0 OID 85046)
-- Dependencies: 570
-- Data for Name: status_tramitacao; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.status_tramitacao VALUES (1, 'Aguardando Deliberação', true, '#bfe1f6ff');
INSERT INTO sup_leg.status_tramitacao VALUES (2, 'Deliberado em Comissão', true, '#d4edbcff');
INSERT INTO sup_leg.status_tramitacao VALUES (3, 'Pedente de Leitura no Expediente', true, '#e8eaedff');
INSERT INTO sup_leg.status_tramitacao VALUES (4, 'Pedente de Deliberação pelas Comissões', true, '#e8eaedff');
INSERT INTO sup_leg.status_tramitacao VALUES (5, '-', true, 'NULL');
INSERT INTO sup_leg.status_tramitacao VALUES (6, 'Deliberado pelas Comissões', true, '#d4edbcff');
INSERT INTO sup_leg.status_tramitacao VALUES (7, 'Manutenção do Veto Total pela CCJ', true, '#e31b1bbf');
INSERT INTO sup_leg.status_tramitacao VALUES (8, 'Rejeição Total do Veto de CCJ', true, '#1dc47eff');
INSERT INTO sup_leg.status_tramitacao VALUES (9, 'Aprovado em Comissão', true, '#1dc47eff');


--
-- TOC entry 4183 (class 0 OID 85053)
-- Dependencies: 572
-- Data for Name: tipo_autor; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.tipo_autor VALUES (1, 'Parlamentar', true);
INSERT INTO sup_leg.tipo_autor VALUES (2, 'Bancada Parlamentar', true);
INSERT INTO sup_leg.tipo_autor VALUES (3, 'Bloco Parlamentar', true);
INSERT INTO sup_leg.tipo_autor VALUES (4, 'Comissão', true);
INSERT INTO sup_leg.tipo_autor VALUES (5, 'Frente Parlamentar', true);
INSERT INTO sup_leg.tipo_autor VALUES (6, 'Órgão', true);
INSERT INTO sup_leg.tipo_autor VALUES (7, 'Chefe do Porder Executivo', true);
INSERT INTO sup_leg.tipo_autor VALUES (8, 'Defensoria do Estado de Roraima', true);
INSERT INTO sup_leg.tipo_autor VALUES (9, 'Externo', true);
INSERT INTO sup_leg.tipo_autor VALUES (10, 'FEMAR', true);
INSERT INTO sup_leg.tipo_autor VALUES (11, 'Mesa Diretora', true);
INSERT INTO sup_leg.tipo_autor VALUES (12, 'Ministério Público de Contas do Estado de Roraima', true);
INSERT INTO sup_leg.tipo_autor VALUES (13, 'Ministério Público do Estado de Roraima', true);
INSERT INTO sup_leg.tipo_autor VALUES (14, 'Outros', true);
INSERT INTO sup_leg.tipo_autor VALUES (15, 'Tribunal de Contas do Estado de Roraima', true);
INSERT INTO sup_leg.tipo_autor VALUES (16, 'Tribunal de Justiça do Estado de Roraima', true);


--
-- TOC entry 4185 (class 0 OID 85058)
-- Dependencies: 574
-- Data for Name: tipo_materia; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.tipo_materia VALUES (1, 'PROJETO DE LEI', true);
INSERT INTO sup_leg.tipo_materia VALUES (2, 'PROJETO DE DECRETO LEGISLATIVO', true);
INSERT INTO sup_leg.tipo_materia VALUES (3, 'MOÇÃO', true);
INSERT INTO sup_leg.tipo_materia VALUES (4, 'INDICAÇÃO', true);
INSERT INTO sup_leg.tipo_materia VALUES (8, 'PEDIDO DE INFORMAÇÃO', true);
INSERT INTO sup_leg.tipo_materia VALUES (10, 'PROJETO DE LEI COMPLEMENTAR', true);
INSERT INTO sup_leg.tipo_materia VALUES (11, 'REQUERIMENTO', true);
INSERT INTO sup_leg.tipo_materia VALUES (12, 'MENSAGEM GOVERNAMENTAL DE VETO', true);
INSERT INTO sup_leg.tipo_materia VALUES (13, 'SUBSTITUTIVO AO PROJETO DE LEI', true);
INSERT INTO sup_leg.tipo_materia VALUES (14, 'PROJETO DE RESOLUÇÃO LEGISLATIVA', true);
INSERT INTO sup_leg.tipo_materia VALUES (15, 'SUBSTITUTIVO A PROPOSTA DE EMENDA À CONSTITUIÇÃO', true);
INSERT INTO sup_leg.tipo_materia VALUES (16, 'PROPOSTA DE EMENDA À CONSTITUIÇÃO', true);
INSERT INTO sup_leg.tipo_materia VALUES (17, 'MENSAGEM GOVERNAMENTAL', true);


--
-- TOC entry 4187 (class 0 OID 85063)
-- Dependencies: 576
-- Data for Name: trecho; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.trecho VALUES (21, '<p class="ql-align-center">EXPLICAÇÕES PESSOAIS.</p>', NULL, 1, '#ffe599ff');
INSERT INTO sup_leg.trecho VALUES (22, '<p>NÃO HAVENDO MAIS NADA A TRATAR, DOU POR ENCERRADA A PRESENTE SESSÃO.</p>', NULL, 1, '#d5a6bdff');
INSERT INTO sup_leg.trecho VALUES (28, '<p class="ql-align-center"><strong>ROTEIRO DO EXMO. SR. PRESIDENTE.</strong></p><p class="ql-align-center"><strong>ABERTURA___________20 MINUTOS</strong>.</p><p class="ql-align-center"><br /></p><p class="ql-align-center"><strong style="background-color:transparent">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À VERIFICAÇÃO DE QUÓRUM.</strong></p><p class="ql-align-center"><br /></p><p class="ql-align-center"><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">HAVENDO QUÓRUM REGIMENTAL, SOB A PROTEÇÃO DE DEUS E EM NOME DO POVO RORAIMENSE, DECLARO ABERTA A PRESENTE SESSÃO.</strong></p><p class="ql-align-center"><br /></p><p class="ql-align-center"><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A(O) SENHOR(A) SEGUNDO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DA ATA DA SESSÃO ANTERIOR.</strong></p><p class="ql-align-center"><br /></p><p class="ql-align-center"><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM DISCUSSÃO, A ATA DA SESSÃO ANTERIOR.</strong></p><p class="ql-align-center"><br /></p><p class="ql-align-center"><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">EM VOTAÇÃO.</strong></p><p class="ql-align-center"><br /></p><p class="ql-align-center"><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">OS DEPUTADOS QUE CONCORDAM COM A ATA DA SESSÃO ANTERIOR PERMANEÇAM COMO ESTÃO.</strong></p><p class="ql-align-center"><strong style="color:rgb( 56 , 118 , 29 );background-color:transparent">APROVADA.</strong></p><p class="ql-align-center"><br /></p><p class="ql-align-center"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À LEITURA DO EXPEDIENTE.</span></p><p class="ql-align-center"><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">EXPEDIENTE _______________ 20 MINUTOS</strong></p><p class="ql-align-center"><br /></p><p class="ql-align-center"><span style="color:rgb( 0 , 0 , 0 );background-color:transparent">SOLICITO A(O) SENHOR(A) PRIMEIRO(A) SECRETÁRIO(A) QUE PROCEDA À CHAMADA DOS ORADORES INSCRITOS PARA O GRANDE EXPEDIENTE.</span></p><p class="ql-align-center"><br /></p><p class="ql-align-center"><strong style="color:rgb( 0 , 0 , 0 );background-color:transparent">GRANDE EXPEDIENTE _______________ 60 MINUTOS</strong></p><p><br /></p>', 2, NULL, 'NULL');


--
-- TOC entry 4189 (class 0 OID 85070)
-- Dependencies: 578
-- Data for Name: usuario; Type: TABLE DATA; Schema: sup_leg; Owner: postgres
--

INSERT INTO sup_leg.usuario VALUES (1, 'admin', '00000', 'Administrador', '$2a$12$STI4qETI2MKiETcoXcMDmOEE5dMYjQLKM21eA3yhUJDmkD5VegEVu', true, 'SUPER_ADMIN');


--
-- TOC entry 4195 (class 0 OID 0)
-- Dependencies: 537
-- Name: abertura_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.abertura_id_seq', 200, false);


--
-- TOC entry 4196 (class 0 OID 0)
-- Dependencies: 539
-- Name: documento_acessorio_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.documento_acessorio_id_seq', 222, true);


--
-- TOC entry 4197 (class 0 OID 0)
-- Dependencies: 541
-- Name: fechamento_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.fechamento_id_seq', 200, false);


--
-- TOC entry 4198 (class 0 OID 0)
-- Dependencies: 543
-- Name: link_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.link_id_seq', 213, true);


--
-- TOC entry 4199 (class 0 OID 0)
-- Dependencies: 545
-- Name: log_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.log_id_seq', 226, true);


--
-- TOC entry 4200 (class 0 OID 0)
-- Dependencies: 547
-- Name: mesa_modelo_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.mesa_modelo_id_seq', 200, false);


--
-- TOC entry 4201 (class 0 OID 0)
-- Dependencies: 549
-- Name: modelo_roteiro_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.modelo_roteiro_id_seq', 200, true);


--
-- TOC entry 4202 (class 0 OID 0)
-- Dependencies: 553
-- Name: ordem_do_dia_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.ordem_do_dia_id_seq', 200, true);


--
-- TOC entry 4203 (class 0 OID 0)
-- Dependencies: 551
-- Name: ordem_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.ordem_id_seq', 222, true);


--
-- TOC entry 4204 (class 0 OID 0)
-- Dependencies: 555
-- Name: parecer_procuradoria_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.parecer_procuradoria_id_seq', 200, false);


--
-- TOC entry 4205 (class 0 OID 0)
-- Dependencies: 557
-- Name: parlamentar_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.parlamentar_id_seq', 200, false);


--
-- TOC entry 4206 (class 0 OID 0)
-- Dependencies: 559
-- Name: roteiro_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.roteiro_id_seq', 200, true);


--
-- TOC entry 4207 (class 0 OID 0)
-- Dependencies: 561
-- Name: roteiro_materia_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.roteiro_materia_id_seq', 200, false);


--
-- TOC entry 4208 (class 0 OID 0)
-- Dependencies: 563
-- Name: roteiro_secretario_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.roteiro_secretario_id_seq', 200, true);


--
-- TOC entry 4209 (class 0 OID 0)
-- Dependencies: 565
-- Name: sessao_legislativa_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.sessao_legislativa_id_seq', 200, false);


--
-- TOC entry 4210 (class 0 OID 0)
-- Dependencies: 567
-- Name: status_materia_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.status_materia_id_seq', 200, false);


--
-- TOC entry 4211 (class 0 OID 0)
-- Dependencies: 569
-- Name: status_tramitacao_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.status_tramitacao_id_seq', 200, false);


--
-- TOC entry 4212 (class 0 OID 0)
-- Dependencies: 571
-- Name: tipo_autor_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.tipo_autor_id_seq', 200, false);


--
-- TOC entry 4213 (class 0 OID 0)
-- Dependencies: 573
-- Name: tipo_materia_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.tipo_materia_id_seq', 200, false);


--
-- TOC entry 4214 (class 0 OID 0)
-- Dependencies: 575
-- Name: trecho_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.trecho_id_seq', 200, false);


--
-- TOC entry 4215 (class 0 OID 0)
-- Dependencies: 577
-- Name: usuario_id_seq; Type: SEQUENCE SET; Schema: sup_leg; Owner: postgres
--

SELECT pg_catalog.setval('sup_leg.usuario_id_seq', 200, false);


--
-- TOC entry 3937 (class 2606 OID 85077)
-- Name: abertura abertura_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.abertura
    ADD CONSTRAINT abertura_pkey PRIMARY KEY (id);


--
-- TOC entry 3939 (class 2606 OID 85079)
-- Name: documento_acessorio documento_acessorio_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.documento_acessorio
    ADD CONSTRAINT documento_acessorio_pkey PRIMARY KEY (id);


--
-- TOC entry 3941 (class 2606 OID 85081)
-- Name: fechamento fechamento_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.fechamento
    ADD CONSTRAINT fechamento_pkey PRIMARY KEY (id);


--
-- TOC entry 3943 (class 2606 OID 85083)
-- Name: link link_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.link
    ADD CONSTRAINT link_pkey PRIMARY KEY (id);


--
-- TOC entry 3945 (class 2606 OID 85085)
-- Name: log log_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.log
    ADD CONSTRAINT log_pkey PRIMARY KEY (id);


--
-- TOC entry 3947 (class 2606 OID 85087)
-- Name: mesa_modelo mesa_modelo_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.mesa_modelo
    ADD CONSTRAINT mesa_modelo_pkey PRIMARY KEY (id);


--
-- TOC entry 3949 (class 2606 OID 85089)
-- Name: modelo_roteiro modelo_roteiro_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.modelo_roteiro
    ADD CONSTRAINT modelo_roteiro_pkey PRIMARY KEY (id);


--
-- TOC entry 3953 (class 2606 OID 85091)
-- Name: ordem_do_dia ordem_do_dia_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.ordem_do_dia
    ADD CONSTRAINT ordem_do_dia_pkey PRIMARY KEY (id);


--
-- TOC entry 3951 (class 2606 OID 85093)
-- Name: ordem ordem_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.ordem
    ADD CONSTRAINT ordem_pkey PRIMARY KEY (id);


--
-- TOC entry 3955 (class 2606 OID 85095)
-- Name: parecer_procuradoria parecer_procuradoria_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.parecer_procuradoria
    ADD CONSTRAINT parecer_procuradoria_pkey PRIMARY KEY (id);


--
-- TOC entry 3957 (class 2606 OID 85097)
-- Name: parlamentar parlamentar_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.parlamentar
    ADD CONSTRAINT parlamentar_pkey PRIMARY KEY (id);


--
-- TOC entry 3961 (class 2606 OID 85099)
-- Name: roteiro_materia roteiro_materia_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.roteiro_materia
    ADD CONSTRAINT roteiro_materia_pkey PRIMARY KEY (id);


--
-- TOC entry 3959 (class 2606 OID 85101)
-- Name: roteiro roteiro_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.roteiro
    ADD CONSTRAINT roteiro_pkey PRIMARY KEY (id);


--
-- TOC entry 3963 (class 2606 OID 85103)
-- Name: roteiro_secretario roteiro_secretario_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.roteiro_secretario
    ADD CONSTRAINT roteiro_secretario_pkey PRIMARY KEY (id);


--
-- TOC entry 3965 (class 2606 OID 85105)
-- Name: sessao_legislativa sessao_legislativa_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.sessao_legislativa
    ADD CONSTRAINT sessao_legislativa_pkey PRIMARY KEY (id);


--
-- TOC entry 3967 (class 2606 OID 85107)
-- Name: status_materia status_materia_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.status_materia
    ADD CONSTRAINT status_materia_pkey PRIMARY KEY (id);


--
-- TOC entry 3969 (class 2606 OID 85109)
-- Name: status_tramitacao status_tramitacao_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.status_tramitacao
    ADD CONSTRAINT status_tramitacao_pkey PRIMARY KEY (id);


--
-- TOC entry 3971 (class 2606 OID 85111)
-- Name: tipo_autor tipo_autor_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.tipo_autor
    ADD CONSTRAINT tipo_autor_pkey PRIMARY KEY (id);


--
-- TOC entry 3973 (class 2606 OID 85113)
-- Name: tipo_materia tipo_materia_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.tipo_materia
    ADD CONSTRAINT tipo_materia_pkey PRIMARY KEY (id);


--
-- TOC entry 3975 (class 2606 OID 85115)
-- Name: trecho trecho_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.trecho
    ADD CONSTRAINT trecho_pkey PRIMARY KEY (id);


--
-- TOC entry 3977 (class 2606 OID 85117)
-- Name: usuario uk_g1orfqvgih1w8s3vyg15fq2b8; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.usuario
    ADD CONSTRAINT uk_g1orfqvgih1w8s3vyg15fq2b8 UNIQUE (login);


--
-- TOC entry 3979 (class 2606 OID 85119)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 3982 (class 2606 OID 85120)
-- Name: link fk4g7h5cho8u2e12xyblehfgxwv; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.link
    ADD CONSTRAINT fk4g7h5cho8u2e12xyblehfgxwv FOREIGN KEY (documento_acessorio_id) REFERENCES sup_leg.documento_acessorio(id);


--
-- TOC entry 3980 (class 2606 OID 85125)
-- Name: documento_acessorio fk5ee21me2c8tl8wkgqxf3q9n6u; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.documento_acessorio
    ADD CONSTRAINT fk5ee21me2c8tl8wkgqxf3q9n6u FOREIGN KEY (id_ordem) REFERENCES sup_leg.ordem(id);


--
-- TOC entry 3984 (class 2606 OID 85130)
-- Name: mesa_modelo fk679nmpyniuee26i4810nhg2od; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.mesa_modelo
    ADD CONSTRAINT fk679nmpyniuee26i4810nhg2od FOREIGN KEY (vice_presidente_2) REFERENCES sup_leg.parlamentar(id);


--
-- TOC entry 4003 (class 2606 OID 85135)
-- Name: roteiro_secretario fk6w7xn5mvqadwk9e4suocnw982; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.roteiro_secretario
    ADD CONSTRAINT fk6w7xn5mvqadwk9e4suocnw982 FOREIGN KEY (id_roteiro) REFERENCES sup_leg.roteiro(id);


--
-- TOC entry 3995 (class 2606 OID 85140)
-- Name: ordem fk81fkma1kfuasscyyjfynig5qq; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.ordem
    ADD CONSTRAINT fk81fkma1kfuasscyyjfynig5qq FOREIGN KEY (ordem_dia) REFERENCES sup_leg.ordem_do_dia(id);


--
-- TOC entry 4004 (class 2606 OID 85145)
-- Name: trecho fk8xdmd0did3sas691hlsceuvhb; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.trecho
    ADD CONSTRAINT fk8xdmd0did3sas691hlsceuvhb FOREIGN KEY (abertura_id) REFERENCES sup_leg.abertura(id);


--
-- TOC entry 3985 (class 2606 OID 85150)
-- Name: mesa_modelo fkb0e310yvkxq6r6e3b5r2qfo3w; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.mesa_modelo
    ADD CONSTRAINT fkb0e310yvkxq6r6e3b5r2qfo3w FOREIGN KEY (corregedor) REFERENCES sup_leg.parlamentar(id);


--
-- TOC entry 3986 (class 2606 OID 85155)
-- Name: mesa_modelo fkbhh0b2ytla76gq5abotl4areg; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.mesa_modelo
    ADD CONSTRAINT fkbhh0b2ytla76gq5abotl4areg FOREIGN KEY (secretario_1) REFERENCES sup_leg.parlamentar(id);


--
-- TOC entry 3981 (class 2606 OID 85160)
-- Name: documento_acessorio fkdch3llio370hcegk29y1nx3ly; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.documento_acessorio
    ADD CONSTRAINT fkdch3llio370hcegk29y1nx3ly FOREIGN KEY (roteiro_secretario) REFERENCES sup_leg.roteiro_secretario(id);


--
-- TOC entry 3987 (class 2606 OID 85165)
-- Name: mesa_modelo fkds9duiqtm6iofh2afjsc4veqv; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.mesa_modelo
    ADD CONSTRAINT fkds9duiqtm6iofh2afjsc4veqv FOREIGN KEY (presidente) REFERENCES sup_leg.parlamentar(id);


--
-- TOC entry 3988 (class 2606 OID 85170)
-- Name: mesa_modelo fkdsoxx6xn0y3oq0k13v3huafxs; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.mesa_modelo
    ADD CONSTRAINT fkdsoxx6xn0y3oq0k13v3huafxs FOREIGN KEY (secretario_4) REFERENCES sup_leg.parlamentar(id);


--
-- TOC entry 4002 (class 2606 OID 85175)
-- Name: roteiro_materia fkfwb62e1ie3sr68g9q4p2kca85; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.roteiro_materia
    ADD CONSTRAINT fkfwb62e1ie3sr68g9q4p2kca85 FOREIGN KEY (tipo_materia) REFERENCES sup_leg.tipo_materia(id);


--
-- TOC entry 4005 (class 2606 OID 85180)
-- Name: trecho fkha7ir5sweu74koqp66nn39ksg; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.trecho
    ADD CONSTRAINT fkha7ir5sweu74koqp66nn39ksg FOREIGN KEY (fechamento_id) REFERENCES sup_leg.fechamento(id);


--
-- TOC entry 3996 (class 2606 OID 85185)
-- Name: ordem fki51cdrpxnj4qptkhbn7mfbdpc; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.ordem
    ADD CONSTRAINT fki51cdrpxnj4qptkhbn7mfbdpc FOREIGN KEY (status_tramitacao) REFERENCES sup_leg.status_tramitacao(id);


--
-- TOC entry 4000 (class 2606 OID 85190)
-- Name: roteiro fkipda2608b0g7e568alblqyrnx; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.roteiro
    ADD CONSTRAINT fkipda2608b0g7e568alblqyrnx FOREIGN KEY (id_ordem_dia) REFERENCES sup_leg.ordem_do_dia(id);


--
-- TOC entry 3993 (class 2606 OID 85195)
-- Name: modelo_roteiro fkiqeumgk4ym0ipslwagdx0kyu9; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.modelo_roteiro
    ADD CONSTRAINT fkiqeumgk4ym0ipslwagdx0kyu9 FOREIGN KEY (abertura_id) REFERENCES sup_leg.abertura(id);


--
-- TOC entry 3997 (class 2606 OID 85200)
-- Name: ordem fkiynnvbhefcoqrljsw5of582k8; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.ordem
    ADD CONSTRAINT fkiynnvbhefcoqrljsw5of582k8 FOREIGN KEY (roteiro_materia) REFERENCES sup_leg.roteiro_materia(id);


--
-- TOC entry 3989 (class 2606 OID 85205)
-- Name: mesa_modelo fkjc4xej95p3ya5gllno1u3tgyv; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.mesa_modelo
    ADD CONSTRAINT fkjc4xej95p3ya5gllno1u3tgyv FOREIGN KEY (vice_presidente_3) REFERENCES sup_leg.parlamentar(id);


--
-- TOC entry 3994 (class 2606 OID 85210)
-- Name: modelo_roteiro fkkg4484js2rjm14m9stq4x6aul; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.modelo_roteiro
    ADD CONSTRAINT fkkg4484js2rjm14m9stq4x6aul FOREIGN KEY (fechamento_id) REFERENCES sup_leg.fechamento(id);


--
-- TOC entry 3998 (class 2606 OID 85215)
-- Name: ordem fkluomlkbmyr4ticse4f9e6pxj2; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.ordem
    ADD CONSTRAINT fkluomlkbmyr4ticse4f9e6pxj2 FOREIGN KEY (status_materia) REFERENCES sup_leg.status_materia(id);


--
-- TOC entry 3990 (class 2606 OID 85220)
-- Name: mesa_modelo fkm9iudx6umdpbj15q256uo3vup; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.mesa_modelo
    ADD CONSTRAINT fkm9iudx6umdpbj15q256uo3vup FOREIGN KEY (vice_presidente_1) REFERENCES sup_leg.parlamentar(id);


--
-- TOC entry 3991 (class 2606 OID 85225)
-- Name: mesa_modelo fkngwahw33n69bw3mitkf4rw0cm; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.mesa_modelo
    ADD CONSTRAINT fkngwahw33n69bw3mitkf4rw0cm FOREIGN KEY (secretario_3) REFERENCES sup_leg.parlamentar(id);


--
-- TOC entry 3983 (class 2606 OID 85230)
-- Name: log fko1w8pjo54gj42ufm7yb43bamb; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.log
    ADD CONSTRAINT fko1w8pjo54gj42ufm7yb43bamb FOREIGN KEY (usuario_id) REFERENCES sup_leg.usuario(id);


--
-- TOC entry 4001 (class 2606 OID 85235)
-- Name: roteiro fkponfa9hjluqrhcgnaaceej7au; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.roteiro
    ADD CONSTRAINT fkponfa9hjluqrhcgnaaceej7au FOREIGN KEY (id_modelo_roteiro) REFERENCES sup_leg.modelo_roteiro(id);


--
-- TOC entry 3999 (class 2606 OID 85240)
-- Name: ordem fkq7vipbp064ltw9ad42c185l81; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.ordem
    ADD CONSTRAINT fkq7vipbp064ltw9ad42c185l81 FOREIGN KEY (parecer_procuradoria) REFERENCES sup_leg.parecer_procuradoria(id);


--
-- TOC entry 3992 (class 2606 OID 85245)
-- Name: mesa_modelo fkuxpohsgwptqijfhfei74kesx; Type: FK CONSTRAINT; Schema: sup_leg; Owner: postgres
--

ALTER TABLE ONLY sup_leg.mesa_modelo
    ADD CONSTRAINT fkuxpohsgwptqijfhfei74kesx FOREIGN KEY (secretario_2) REFERENCES sup_leg.parlamentar(id);


-- Completed on 2024-06-03 10:46:08

--
-- PostgreSQL database dump complete
--

