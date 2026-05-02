# FastTripPlanner

O **FastTripPlanner** é um aplicativo Android nativo projetado para ajudar os usuários a planejarem e calcularem rapidamente os custos de suas viagens. De forma intuitiva, o usuário informa os dados básicos da viagem, escolhe o padrão de hospedagem e os serviços desejados, e o app gera um relatório financeiro completo.

## Funcionalidades

* **Coleta de Dados:** Entrada simples de destino, número de dias e orçamento diário.
* **Validação de Interface:** Bloqueio de avanço caso haja campos vazios.
* **Personalização da Viagem:** * Escolha do tipo de hospedagem através de um menu suspenso (Dropdown).
    * Seleção múltipla de serviços extras através de caixas de seleção (Checkboxes).
* **Cálculo Automático:** Motor de cálculo interno que aplica multiplicadores de hospedagem e soma custos de serviços extras ao orçamento base.
* **Resumo Detalhado:** Tela final com o relatório completo da viagem e o custo total estimado, com a opção de reiniciar a simulação.

## Tecnologias e Ferramentas

* **Linguagem:** Kotlin
* **Interface Gráfica:** Jetpack Compose (Material Design 3)
* **Navegação:** Android Intents genéricas para transição entre Activities
* **Arquitetura de Dados:** Enum Classes para tipagem forte de opções e valores.
* **SDK Mínimo:** API 26 (Android 8.0 Oreo)

## Estrutura de Telas (Fluxo do App)

O aplicativo é dividido em três telas principais (`Activities`), garantindo uma separação clara de responsabilidades:

1.  **`TripDataActivity` (Entrada de Dados):**
    * A tela inicial (Launcher) do aplicativo.
    * Coleta o Destino, Número de Dias e Orçamento Diário.
    * Garante que o usuário preencha todos os campos antes de avançar para a próxima etapa.
2.  **`TripOptionsActivity` (Opções e Cálculo):**
    * O usuário seleciona o Tipo de Hospedagem (única escolha).
    * O usuário seleciona os Serviços Extras (múltipla escolha).
    * Possui botões para voltar à tela anterior ou calcular o roteiro final.
3.  **`TripResumeActivity` (Relatório Final):**
    * Exibe todos os dados formatados em campos de leitura (`readOnly`).
    * Mostra o **Custo Total** calculado da viagem.
    * Permite "Reiniciar Cálculo", limpando a pilha de navegação e voltando à tela inicial.

## Lógica de Cálculo (Regras de Negócio)

O custo total da viagem é calculado com base nas opções selecionadas pelo usuário, guiado por classes de modelo (`Enum`). A fórmula utilizada é:

$$Custo Total = (Orçamento Diário \times Dias \times Multiplicador da Hospedagem) + Soma dos Serviços Extras$$

### Multiplicadores de Hospedagem (`HostingType`)
* **Econômica:** 1.0x (Não altera o valor base)
* **Conforto:** 1.5x (+50% no valor base)
* **Luxo:** 2.2x (+120% no valor base)

### Custos de Serviços Extras (`ServiceType`)
* **Transporte:** R$ 300,00
* **Alimentação:** R$ 50,00
* **Turismo:** R$ 120,00

## Como executar o projeto

1.  Clone este repositório:
    ```bash
    git clone https://github.com/Rhuan-aa/FastTripPlanner.git
    ```
2.  Abra o projeto no **Android Studio** (Recomendado versão Iguana ou superior).
3.  Aguarde a sincronização do Gradle (Sync).
4.  Selecione um emulador (API 26+) ou dispositivo físico conectado.
5.  Clique em **Run** (`Shift + F10`).

## Vídeo de exemplo 
   ```bash
   https://youtube.com/shorts/oE71JvUejrQ?feature=share
   ```
---
**Desenvolvido por:** Rhuan

---
