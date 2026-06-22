# Instalador final

Use este arquivo para entregar o projeto:

```text
GerenciadorEstoque-Setup.exe
```

Se quiser instalar e tentar abrir o app automaticamente ao final, execute:

```text
INSTALAR_E_ABRIR.bat
```

Observacao: o instalador gerado por `jpackage` nao possui uma opcao nativa para abrir o aplicativo automaticamente no ultimo passo da instalacao. Por isso o arquivo `.bat` executa o instalador, espera terminar e abre o programa instalado quando encontrar o executavel.

Banco de dados usado pelo aplicativo instalado:

```text
%APPDATA%\GerenciadorEstoque\Estoque.db
```

Esse caminho evita erro de permissao ao tentar abrir o banco dentro da pasta de instalacao.
