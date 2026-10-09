# HydroTrack — Guia de Design

Sistema visual completo do HydroTrack, app de acompanhamento de consumo diário de água. Tudo aqui foi extraído diretamente do código do protótipo (`src/styles.css`, `src/routes/index.tsx`, `src/components/ui/button.tsx`).

---

## 1. Identidade

| Aspecto | Valor |
|---|---|
| Nome do produto | HydroTrack |
| Tagline | "Seu bem-estar, gota a gota." |
| Tema | Claro e escuro (segue a preferência do sistema, `prefers-color-scheme`) |
| Direção visual | Aquático / azul-gota, limpo, arredondado e amigável |
| Idioma da interface | Português (pt-BR) |

---

## 2. Tipografia

| Uso | Fonte | Pesos |
|---|---|---|
| Interface inteira | **Roboto** | 400, 500, 600, 700, 800 |

- Fonte carregada via Google Fonts (`display=swap`).
- Fallback: `sans-serif`.

### Escala de texto usada na interface

| Elemento | Tamanho | Peso |
|---|---|---|
| Logotipo (texto) | `text-lg` (18px) | Bold (700) |
| Título da página | `text-[28px]` → `text-4xl` (36px) em telas grandes | Bold (700) |
| Títulos de seção | `text-xl` (20px) | Bold (700) |
| Rótulo "eyebrow" | `text-xs` (12px) | Semibold (600), caixa alta, cor primária |
| Subtítulos e textos de apoio | `text-sm` (14px) | Regular (400) / Medium (500) |
| Percentual grande do anel | 48px | Bold (700), tabular-nums |
| Volume grande do anel | 25px | Bold (700), tabular-nums |
| Valores numéricos | `text-xl` (20px) | Bold (700), tabular-nums |
| Etiquetas pequenas / legendas | `text-xs` / 11px | Medium (400–500) |

> Números sempre com `tabular-nums` para alinhamento estável.

---

## 3. Cores

Os valores no código usam **OKLCH**; abaixo estão os equivalentes aproximados em HEX para referência visual rápida. Sempre que possível, use o token, não o HEX.

### 3.1 Tema Claro

| Token | HEX aprox. | OKLCH (código) | Uso |
|---|---|---|---|
| `--background` | `#F3F9FD` | `oklch(0.978 0.009 240)` | Fundo geral da página |
| `--foreground` | `#0F293A` | `oklch(0.27 0.045 239)` | Texto principal |
| `--card` | `#FFFFFF` | `oklch(1 0 0)` | Cartões, listas, botão central do anel |
| `--card-foreground` | `#0F293A` | `oklch(0.27 0.045 239)` | Texto sobre cartões |
| `--popover` | `#FFFFFF` | `oklch(1 0 0)` | Fundo de popovers/modais |
| `--primary` | `#008ADF` | `oklch(0.61 0.17 243)` | Azul principal: anel de progresso, botões "Registrar", ícones de água |
| `--primary-foreground` | `#FFFFFF` | `oklch(1 0 0)` | Texto sobre o azul primário |
| `--secondary` | `#D4F0FE` | `oklch(0.94 0.035 229)` | Azul suave secundário |
| `--secondary-foreground` | `#00476D` | `oklch(0.38 0.09 239)` | Texto sobre secundário |
| `--muted` | `#E9F1F7` | `oklch(0.955 0.012 238)` | Fundo neutro suave |
| `--muted-foreground` | `#596C78` | `oklch(0.52 0.03 238)` | Textos secundários, legendas |
| `--accent` | `#CCEAFC` | `oklch(0.92 0.04 235)` | Destaque de hover / realce |
| `--accent-foreground` | `#004A74` | `oklch(0.39 0.1 239)` | Texto sobre accent |
| `--destructive` | `#DB373A` | `oklch(0.59 0.2 25)` | Ações destrutivas (ex.: excluir registro) |
| `--destructive-foreground` | `#FFFFFF` | `oklch(1 0 0)` | Texto sobre destructive |
| `--border` | `#D7E3EC` | `oklch(0.91 0.018 236)` | Bordas de cartões e divisórias |
| `--input` | `#D2E1E9` | `oklch(0.9 0.02 234)` | Bordas de campos de entrada |
| `--ring` | `#008ADF` | `oklch(0.61 0.17 243)` | Foco de acessibilidade (focus ring) |
| `--water-soft` | `#CCF1FF` | `oklch(0.94 0.05 236)` | Fundo azul suave de ícones/botões de água |
| `--water-line` | `#A5D2EC` | `oklch(0.84 0.06 234)` | Bordas suaves dos blocos de água |
| `--surface-tint` | `#E4F6FF` | `oklch(0.964 0.025 234)` | Tinta do gradiente radial do fundo |
| `--success` | `#008E5A` | `oklch(0.56 0.15 163)` | Verde de "meta alcançada" |
| `--success-soft` | `#CBF7E1` | `oklch(0.94 0.055 163)` | Fundo verde suave |
| `--celebration` | `#DA8500` | `oklch(0.69 0.16 69)` | Âmbar/laranja de "meta ultrapassada" |
| `--celebration-soft` | `#FFEFCB` | `oklch(0.96 0.052 80)` | Fundo âmbar suave |
| `--water-track` | `#CCE6F5` | `oklch(0.91 0.035 234)` | Trilho do anel de progresso (antes de preencher) |

### 3.2 Tema Escuro

| Token | HEX aprox. | OKLCH (código) | Uso |
|---|---|---|---|
| `--background` | `#0A151E` | `oklch(0.19 0.025 244)` | Fundo geral |
| `--foreground` | `#E5F1F7` | `oklch(0.95 0.015 230)` | Texto principal |
| `--card` | `#15232F` | `oklch(0.25 0.03 243)` | Cartões |
| `--card-foreground` | `#E5F1F7` | `oklch(0.95 0.015 230)` | Texto sobre cartões |
| `--popover` | `#15232F` | `oklch(0.25 0.03 243)` | Popovers/modais |
| `--popover-foreground` | `#E5F1F7` | `oklch(0.95 0.015 230)` | Texto de popovers |
| `--primary` | `#50BDF8` | `oklch(0.76 0.13 236)` | Azul principal (mais claro no escuro) |
| `--primary-foreground` | `#011322` | `oklch(0.18 0.04 243)` | Texto sobre o azul primário |
| `--secondary` | `#1D3644` | `oklch(0.32 0.04 235)` | Secundário |
| `--secondary-foreground` | `#DBEBF3` | `oklch(0.93 0.02 230)` | Texto sobre secundário |
| `--muted` | `#22303A` | `oklch(0.3 0.026 239)` | Neutro suave |
| `--muted-foreground` | `#9CAEB8` | `oklch(0.74 0.025 232)` | Textos secundários |
| `--accent` | `#233E50` | `oklch(0.35 0.045 239)` | Hover / realce |
| `--accent-foreground` | `#E5F1F7` | `oklch(0.95 0.015 230)` | Texto sobre accent |
| `--destructive` | `#FF7670` | `oklch(0.73 0.17 25)` | Destrutivo (mais claro no escuro) |
| `--destructive-foreground` | `#071117` | `oklch(0.17 0.02 240)` | Texto sobre destructive |
| `--border` | `#344551` | `oklch(0.38 0.03 239)` | Bordas |
| `--input` | `#3E505C` | `oklch(0.42 0.03 239)` | Bordas de campos |
| `--ring` | `#50BDF8` | `oklch(0.76 0.13 236)` | Foco |
| `--water-soft` | `#0F374C` | `oklch(0.32 0.058 236)` | Fundo azul suave |
| `--water-line` | `#2A5871` | `oklch(0.44 0.065 234)` | Bordas suaves dos blocos de água |
| `--surface-tint` | `#0F2C3D` | `oklch(0.28 0.047 238)` | Tinta do gradiente radial do fundo |
| `--success` | `#3FC996` | `oklch(0.75 0.14 164)` | Verde de sucesso |
| `--success-soft` | `#073424` | `oklch(0.29 0.056 164)` | Fundo verde suave |
| `--celebration` | `#F6BC5D` | `oklch(0.83 0.13 78)` | Âmbar de celebração |
| `--celebration-soft` | `#3F2D10` | `oklch(0.31 0.05 76)` | Fundo âmbar suave |
| `--water-track` | `#294354` | `oklch(0.37 0.043 237)` | Trilho do anel |

### 3.3 Estados de progresso

| Estado | Cor do anel | Selo | Mensagem |
|---|---|---|---|
| Em andamento | `--primary` | `bg-water-soft` + `text-primary` | "Sua hidratação de hoje" |
| Meta alcançada | `--success` | `bg-success-soft` + `text-success` | "Meta alcançada!" |
| Meta ultrapassada | `--celebration` | `bg-celebration-soft` + `text-celebration` | "Meta ultrapassada!" |

---

## 4. Fundo da página

Gradiente radial aquoso aplicado ao contêiner principal (utility `water-shell`):

```css
background: radial-gradient(circle at 50% 16%, var(--surface-tint), var(--background) 55%);
```

Ou seja: um halo azul-claro concentrado no topo central, dissolvendo para o fundo padrão.

---

## 5. Raios de borda (arredondamento)

| Elemento | Raio |
|---|---|
| Base do sistema | `1rem` (16px) |
| Cartões, listas, painéis | `rounded-2xl` (16px) |
| Botões e campos de entrada | `rounded-xl` (12px) |
| Blocos de ícone (quadrados suaves) | `rounded-xl` (12px) |
| Botão central do anel / botões redondos | `rounded-full` |
| Modais | `rounded-3xl` (24px) |

---

## 6. Sombras e brilhos

| Efeito | Definição |
|---|---|
| Sombra padrão de cartão | `shadow-sm` (sutil) |
| Sombra do anel de progresso | `drop-shadow(0 5px 12px azul-primário @ 22%)` — utility `water-ring-glow` |
| Brilho de celebração | `drop-shadow(0 4px 14px âmbar @ 38%)` — utility `celebration-glow` |
| Sombra do cartão central do anel | `0 12px 35px` azul primário @ 9% |
| Cartões de registro rápido | Sem sombra (`shadow-none` nos outline), com borda `water-line` |

---

## 7. Componentes

### 7.1 Botões (variantes)

| Variante | Estilo |
|---|---|
| `default` | Fundo `--primary`, texto branco, sombra suave, hover 90% |
| `water` | Igual ao default + `active:scale-[0.98]` (clique afundando) |
| `waterSoft` | Fundo `--water-soft`, texto `--primary`, hover esmaece, ícone gira −6° no hover, elevação de 2px no hover |
| `outline` | Fundo `--background`, borda `--input`, hover com `--accent` |
| `ghost` | Sem fundo, hover com `--accent` (usado no excluir registro) |
| `ring` | Totalmente redondo, com foco de 4px |

| Tamanho | Medidas |
|---|---|
| `default` | altura 36px, padding horizontal 16px |
| `sm` | altura 32px, texto 12px |
| `lg` | altura 40px |
| `icon` | 36 × 36px (usado no lápis de editar meta, formato circular) |
| `tile` | bloco de escolha rápida: min. 112px de altura, coluna, cantos 16px, ícone 24px + rótulo grande + legenda pequena |

### 7.2 Anel de progresso (componente principal)

- SVG 304 × 304, círculo com raio 116, espessura de traço 16.
- Trilho: `--water-track`; progresso: `--primary` (ou success/celebration conforme estado).
- Pontas arredondadas (`strokeLinecap: round`), rotacionado −90° para começar no topo.
- Transição de 700 ms com ease-out na mudança de preenchimento e cor.
- Centro: cartão branco/redondo com ícone de gota, rótulo "Consumo diário de água" e o número (toque alterna entre % e ml).

### 7.3 Cartões de escolha rápida (200 / 300 / 500 ml)

- Grade de 3 colunas (2 no mobile), gap 10px.
- `bg-water-soft`, borda `water-line`, cantos 16px, ícone no topo + "+ X ml" em negrito + legenda pequena.
- Elevação suave no hover (`hover:-translate-y-0.5`) e leve rotação do ícone.

### 7.4 Lista de registros

- Cartão branco (`--card`) com cantos 16px, divisórias `--border` entre itens.
- Cada item: ícone em quadrado `water-soft` 40px, valor "+ X ml" em negrito, horário pequeno em `muted-foreground`, botão de lixeira à direita (hover em `--destructive`).
- Rodapé com "Total do dia" separado por borda.
- Estado vazio: cartão tracejado (`border-dashed`, borda `water-line`) com ícone de copo em círculo `water-soft` e textos de convite.

### 7.5 Painel de resumo (consumido hoje / meta)

- Cartão único dividido em duas colunas por divisória vertical `--border`, cantos 16px, rótulo pequeno em cima e valor grande embaixo.

### 7.6 Modal (alterar meta)

- Largura máx. 384px, cantos 24px, fundo `--card`, ícone de lápis em quadrado `water-soft`, campo com sufixo "ml" e botão primário de largura total.

### 7.7 Ícones

- Biblioteca: **Lucide** (`lucide-react`).
- Usados: `Droplet` (gota, ícone-assinatura), `GlassWater` (copo), `BottleWine` (garrafa, para 500 ml ou mais), `Plus`, `Pencil`, `Trash2`, `Check`, `Sparkles`, `ChevronRight`, `CircleHelp`.
- Traço: 1.7 para ícones suaves; a gota usa `fill-current` para ficar sólida.

---

## 8. Animações

| Animação | Definição |
|---|---|
| `float-drop` | Gota flutua suavemente: sobe e desce 5px em ciclo de 3s, ease-in-out, infinita |
| Anel de progresso | Transição de `stroke-dashoffset` e cor em 700ms ease-out |
| Botões `water`/`waterSoft` | Escala 0.98 ao pressionar |
| Blocos de água | Elevação de 2px + rotação de ícone no hover |
| Acessibilidade | Com `prefers-reduced-motion`, todas as animações e transições são praticamente desativadas |

---

## 9. Espaçamento e layout

| Medida | Valor |
|---|---|
| Largura máxima do conteúdo | 960px, centralizado |
| Padding lateral da página | 16px (mobile) → 32px (≥640px) |
| Espaço superior da página | 32px (mobile) → 48px (≥640px) |
| Espaço inferior da página | 80px |
| Grade principal | 2 colunas a partir de 1024px: progresso (1fr) + ações (0.9fr), gap 48px |
| Espaço entre seções | 36px (`space-y-9`) |
| Anel de progresso | Quadrado, máx. 304px, centralizado |

---

## 10. Copys padrão (pt-BR)

| Contexto | Texto |
|---|---|
| Cabeçalho | "Hoje, {dia de mês}" |
| Subtítulo | "Seu bem-estar, gota a gota." |
| Selo neutro | "Sua hidratação de hoje" |
| Selo sucesso | "Meta alcançada!" |
| Selo celebração | "Meta ultrapassada!" |
| Frase de apoio (em progresso) | "Faltam X ml para sua meta diária." |
| Frase de apoio (dia vazio) | "Comece o dia com um copo de água." |
| Frase de apoio (completo) | "Você alcançou sua meta diária. Muito bem!" |
| Frase de apoio (acima) | "X ml acima da sua meta. Continue se cuidando!" |
| Seção de registro | "Hora de se hidratar" / "Registrar consumo" |
| Seção de histórico | "Seu dia em detalhes" / "Registros de Hoje" |
| Estado vazio | "Nenhum registro ainda" / "Seus copos de água vão aparecer aqui." |
| Rodapé | "Seus registros recomeçam a cada novo dia." |
| Modal | "Alterar meta diária" / "Defina quanto de água deseja beber por dia." |

---

## 11. Resumo em uma linha

**Azul-gota (#008ADF claro / #50BDF8 escuro) sobre fundo azul-acinzentado quase branco (#F3F9FD) ou azul-petróleo profundo (#0A151E), Roboto em todo o lugar, cartões brancos arredondados de 16px, ícones Lucide finos e pastéis suaves para verde de sucesso e âmbar de celebração.**
