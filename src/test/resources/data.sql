-- Inicialização de dados de teste
-- Reset sequence to avoid conflicts
ALTER TABLE tipo_usuario ALTER COLUMN id RESTART WITH 1;

-- Tipos de usuário
INSERT INTO tipo_usuario (nome) VALUES ('PROPRIETARIO');
INSERT INTO tipo_usuario (nome) VALUES ('CLIENTE');