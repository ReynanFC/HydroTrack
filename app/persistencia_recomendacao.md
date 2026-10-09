# Persistência de Dados: WaterProgressView

## Recomendação

Usar **SharedPreferences**, guardando os registros de consumo como uma string JSON.

## Por que combina com o trabalho

- Os dados são pequenos: uma meta, uma data e uma lista com poucos registros por dia.
- Os registros do dia anterior são descartados, então a lista nunca cresce. Não há necessidade de consultas, índices ou histórico.
- É simples de implementar e de explicar na defesa oral.
- Não exige dependências extras: o `org.json` já vem no Android, então a lista vira uma string fácil de salvar e ler.

## O que guardar

| Chave | Tipo | Exemplo |
|---|---|---|
| `meta_diaria` | int (ml) | `2000` |
| `data_ciclo` | String | `"2026-10-08"` |
| `registros` | String (JSON) | `[{"ml":300,"ts":1759930000000}, ...]` |

O consumo acumulado pode ser salvo também, mas o ideal é **recalcular somando os registros** ao carregar. Assim ele nunca fica inconsistente com a lista.

## Como tratar o novo dia

1. Ao abrir o app, compare `data_ciclo` com a data atual do sistema.
2. Se forem diferentes, apague os registros e atualize `data_ciclo`.
3. **Não apague a meta**, pois ela deve continuar valendo nos dias seguintes.
4. Faça essa checagem também no `onResume()`, para cobrir o caso de o app ficar aberto durante a virada do dia.

## Justificativa pronta para a defesa

> "Escolhi SharedPreferences porque o volume de dados é pequeno e descartado a cada dia, não havendo necessidade de consultas relacionais. Isso reduz a complexidade e mantém a persistência entre reinicializações."

## Quando mudar para Room

Se o objetivo fosse manter histórico de vários dias, gráficos semanais e consultas, o **Room** valeria a pena. Para o que o trabalho pede, seria exagero.

## Requisitos do enunciado relacionados

- **Seção 4:** armazenar meta, registros individuais (volume e instante) e consumo acumulado; dados recuperados após reiniciar; justificar a escolha; permitir alterar a meta preservando os registros do dia.
- **Seção 5:** o alerta de ultrapassagem deve se basear nos valores armazenados, não apenas em mudança visual temporária.
- **Seção 9:** distinguir registros de dias diferentes, descartar os de dias anteriores e identificar novo ciclo pela data do sistema.
- **Critério 4 de avaliação:** persistência e consistência após reiniciar o app.
