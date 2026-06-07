--Criação da tabela de categorias dos equipamentos
CREATE TABLE categorias (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL UNIQUE
);

--Inserindo as categorias
INSERT INTO categorias (nome)
VALUES
('ONU'),
('Roteador'),
('Switch'),
('Cabo de Fibra'),
('Cabo de Rede'),
('Conector'),
('Patch Cord'),
('Fonte'),
('Caixa de Emenda'),
('Ferramenta'),
('Equipamento de Rede'),
('Antena'),
('Nobreak'),
('SFP'),
('OLT');


CREATE TABLE equipamentos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL CHECK(length(nome) <= 100),
    descricao TEXT CHECK(length(descricao) <= 255),
    quantidade INTEGER NOT NULL DEFAULT 0,
    categoria_id INTEGER NOT NULL,
    localizacao TEXT CHECK(length(localizacao) <= 100),
    status TEXT NOT NULL DEFAULT 'Disponivel',
    FOREIGN KEY (categoria_id) 
        REFERENCES categorias(id)
);

-- Adicionando um equipamento
INSERT INTO equipamentos (nome, descricao, quantidade, categoria_id, localizacao, status)
VALUES (
    'Roteador TP-Link',
    'Roteador Wi-Fi AC1200',
    15,
    2,
    'Estoque Principal',
    'Disponivel'
);

-- Adicionando vários equipamentos
INSERT INTO equipamentos (nome, descricao, quantidade, categoria_id, localizacao, status)
VALUES
('ONU Intelbras 121W', 'ONU para clientes de fibra óptica', 30, 1, 'Estoque Principal', 'Disponivel'),

('Roteador TP-Link Archer C20', 'Roteador Wi-Fi dual band', 15, 2, 'Estoque Principal', 'Disponivel'),

('Switch Intelbras SG 800 Q+', 'Switch Gigabit de 8 portas', 10, 3, 'Almoxarifado', 'Disponivel'),

('Bobina de Fibra Óptica', 'Bobina com 1km de cabo de fibra óptica', 5, 4, 'Depósito', 'Disponivel'),

('Cabo de Rede Cat6', 'Caixa de cabo de rede Cat6', 20, 5, 'Depósito', 'Disponivel'),

('Conector RJ45', 'Conector para cabo de rede', 200, 6, 'Almoxarifado', 'Disponivel'),

('Patch Cord SC/APC', 'Patch cord óptico para conexões de fibra', 50, 7, 'Estoque Principal', 'Disponivel'),

('Fonte 12V 2A', 'Fonte de alimentação para equipamentos de rede', 25, 8, 'Almoxarifado', 'Disponivel'),

('CTO 16 Portas', 'Caixa de Terminação Óptica', 8, 9, 'Depósito', 'Disponivel'),

('Alicate de Crimpagem', 'Ferramenta para crimpar conectores RJ45', 6, 10, 'Oficina Técnica', 'Disponivel'),

('Conversor de Mídia', 'Equipamento para conversão de sinal óptico', 12, 11, 'Estoque Principal', 'Disponivel'),

('Antena Ubiquiti LiteBeam', 'Antena para enlaces de rádio', 4, 12, 'Depósito', 'Disponivel'),

('Nobreak 1200VA', 'Nobreak para proteção de equipamentos', 3, 13, 'Sala Técnica', 'Disponivel'),

('Módulo SFP Gigabit', 'Módulo SFP para switches e OLTs', 18, 14, 'Estoque Principal', 'Disponivel'),

('OLT Huawei MA5608T', 'Equipamento OLT para gerenciamento da rede GPON', 2, 15, 'Sala Técnica', 'Disponivel');