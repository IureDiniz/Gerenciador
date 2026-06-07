CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL CHECK(length(nome) <= 100),
    email TEXT NOT NULL UNIQUE CHECK(length(email) <= 100),
    senha TEXT NOT NULL CHECK(length(senha) <= 255),
    tipo TEXT NOT NULL CHECK(tipo IN ('Funcionario', 'Supervisor', 'Gerente')),
    ativo INTEGER NOT NULL DEFAULT 1
);

--inserção de um usuario
INSERT INTO usuarios (nome, email, senha, tipo, ativo)
VALUES (
    'Administrador',
    'admin@empresa.com',
    '123456',
    'Supervisor',
    1
);

--adicionando vários usuários
INSERT INTO usuarios (nome, email, senha, tipo, ativo)
VALUES
('Ricardo Menezes', 'ricardo.menezes@provedor.com', 'Gerente@2026', 'Gerente', 1),

('Carlos Henrique', 'carlos.henrique@provedor.com', 'SupCarlos#01', 'Supervisor', 1),

('Mariana Souza', 'mariana.souza@provedor.com', '1Mariana$2026', 'Supervisor', 1),

('João Pedro Lima', 'joao.lima@provedor.com', 'JoaoP123!', 'Funcionario', 1),

('Ana Beatriz Costa', 'ana.costa@provedor.com', 'AnaB456@', 'Funcionario', 0),

('Lucas Martins', 'lucas.martins@provedor.com', 'Lucas789#', 'Funcionario', 1),

('Fernanda Oliveira', 'fernanda.oliveira@provedor.com', 'Fernanda321$', 'Funcionario', 0),

('Gabriel Almeida', 'gabriel.almeida@provedor.com', 'Gabriel654!', 'Funcionario', 1),

('Juliana Ribeiro', 'juliana.ribeiro@provedor.com', 'Juliana987@', 'Funcionario', 1);