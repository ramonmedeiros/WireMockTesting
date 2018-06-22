Feature: Que a função seja executada 
Como  Analista da Área de Taxas
Quero que ocorra a execução de funções necessárias para o cálculo de fórmulas específicas
Para que seja possível obter-se os valores ajustados com funções gerais

Scenario: Realizando funções de apoio aos cálculos
     Given a operacao a ser realizada
     | id modelo | patrimonio_liquido | valor_ol1 |
     | 1         | 50.70              |   60.90   |
     Then retornar o resultado

Scenario: Validando operacoes e valores
     Given a operacao a ser realizada
     | id modelo | patrimonio_liquido | valor_ol1 |
     | 2         | 50.70              |   60.90   |
     Then retornar erro caso as entradas estejam invalidas
