const API_URL = 'http://localhost:8080/api/boards';
const CARDS_API_URL = 'http://localhost:8080/api/cards';

let currentBoardId = null;
let currentColumnId = null;
let currentCardId = null;
let boardData = null;

// Elementos DOM
const boardsList = document.getElementById('boardsList');
const boardDetail = document.getElementById('boardDetail');
const boardTitle = document.getElementById('boardTitle');
const columnsContainer = document.getElementById('columnsContainer');
const modal = document.getElementById('modalNewBoard');
const inputBoardName = document.getElementById('inputBoardName');
const modalCard = document.getElementById('modalCard');
const inputCardTitle = document.getElementById('inputCardTitle');
const inputCardDescription = document.getElementById('inputCardDescription');

// Event Listeners
document.getElementById('btnNewBoard').addEventListener('click', openModal);
document.getElementById('btnCancelModal').addEventListener('click', closeModal);
document.getElementById('btnConfirmCreate').addEventListener('click', createBoard);
document.getElementById('btnBackToList').addEventListener('click', showBoardsList);
document.getElementById('btnDeleteBoard').addEventListener('click', deleteCurrentBoard);
document.getElementById('btnCancelCardModal').addEventListener('click', closeCardModal);
document.getElementById('btnConfirmCard').addEventListener('click', saveCard);

// Funções principais
async function loadBoards() {
    try {
        const response = await fetch(API_URL);
        const boards = await response.json();
        renderBoards(boards);
    } catch (error) {
        console.error('Erro ao carregar boards:', error);
        alert('Erro ao carregar os boards');
    }
}

function renderBoards(boards) {
    boardsList.innerHTML = '';

    if (boards.length === 0) {
        boardsList.innerHTML = '<p style="color: white; text-align: center; grid-column: 1/-1;">Nenhum board encontrado. Crie um novo!</p>';
        return;
    }

    boards.forEach(board => {
        const card = document.createElement('div');
        card.className = 'board-card';
        card.innerHTML = `
            <h3>${board.name}</h3>
            <p>ID: ${board.id}</p>
        `;
        card.addEventListener('click', () => showBoardDetail(board.id, board.name));
        boardsList.appendChild(card);
    });
}

async function showBoardDetail(boardId, boardName) {
    currentBoardId = boardId;
    boardTitle.textContent = boardName;

    try {
        const response = await fetch(`${API_URL}/${boardId}`);
        boardData = await response.json();
        renderColumns(boardData.columns);

        boardsList.style.display = 'none';
        boardDetail.style.display = 'block';
    } catch (error) {
        console.error('Erro ao carregar board:', error);
        alert('Erro ao carregar o board');
    }
}

function renderColumns(columns) {
    columnsContainer.innerHTML = '';

    columns.forEach(column => {
        const columnDiv = document.createElement('div');
        columnDiv.className = 'column';
        columnDiv.dataset.columnId = column.id;

        columnDiv.innerHTML = `
            <h3>${column.name}</h3>
            <button class="btn-add-card" data-column-id="${column.id}">+ Adicionar Card</button>
            <div class="cards-list" data-column-id="${column.id}"></div>
        `;

        const cardsList = columnDiv.querySelector('.cards-list');

        // Eventos de drag and drop
        cardsList.addEventListener('dragover', handleDragOver);
        cardsList.addEventListener('drop', handleDrop);

        // Renderizar cards
        if (column.cards && column.cards.length > 0) {
            column.cards.forEach(card => {
                // { changed code }
                const cardElement = createCardElement(card, column.id);
                cardsList.appendChild(cardElement);
            });
        }

        columnsContainer.appendChild(columnDiv);
    });

    // Adicionar event listeners para botões de adicionar card
    document.querySelectorAll('.btn-add-card').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const columnId = parseInt(e.target.dataset.columnId);
            openCardModal(columnId);
        });
    });
}

// { changed code }
function createCardElement(card, columnId) {
    const cardDiv = document.createElement('div');
    cardDiv.className = 'card-item';
    cardDiv.draggable = true;
    cardDiv.dataset.cardId = card.id;
    // armazena a coluna atual do card (útil se o DTO do servidor não trouxer columnId)
    cardDiv.dataset.columnId = columnId;

    cardDiv.innerHTML = `
        <h4>${card.title}</h4>
        ${card.description ? `<p>${card.description}</p>` : ''}
        <div class="card-actions">
            <button class="btn-icon btn-edit" data-card-id="${card.id}">✏️ Editar</button>
            <button class="btn-icon btn-delete" data-card-id="${card.id}">🗑️</button>
        </div>
    `;

    // Eventos de drag
    cardDiv.addEventListener('dragstart', handleDragStart);
    cardDiv.addEventListener('dragend', handleDragEnd);

    // Evento de editar - passa columnId para manter referência da coluna
    cardDiv.querySelector('.btn-edit').addEventListener('click', (e) => {
        e.stopPropagation();
        openEditCardModal(card, columnId);
    });

    // Evento de deletar
    cardDiv.querySelector('.btn-delete').addEventListener('click', (e) => {
        e.stopPropagation();
        deleteCard(card.id);
    });

    return cardDiv;
}

// { changed code }
function openEditCardModal(card, columnId) {
    currentCardId = card.id;
    // define a coluna atual a partir do parâmetro (ou do próprio card se existir)
    currentColumnId = (typeof columnId !== 'undefined') ? columnId : (card.columnId ?? null);
    document.getElementById('modalCardTitle').textContent = 'Editar Card';
    inputCardTitle.value = card.title;
    inputCardDescription.value = card.description || '';
    modalCard.classList.add('active');
    inputCardTitle.focus();
}

// Drag and Drop
let draggedCard = null;

function handleDragStart(e) {
    draggedCard = e.target;
    e.target.classList.add('dragging');
}

function handleDragEnd(e) {
    e.target.classList.remove('dragging');
}

function handleDragOver(e) {
    e.preventDefault();
}

async function handleDrop(e) {
    e.preventDefault();

    if (!draggedCard) return;

    const targetColumn = e.target.closest('.cards-list');
    if (!targetColumn) return;

    const newColumnId = parseInt(targetColumn.dataset.columnId);
    const cardId = parseInt(draggedCard.dataset.cardId);

    try {
        await fetch(`${CARDS_API_URL}/${cardId}/move?newColumnId=${newColumnId}`, {
            method: 'PUT'
        });

        // Recarregar o board
        showBoardDetail(currentBoardId, boardTitle.textContent);
    } catch (error) {
        console.error('Erro ao mover card:', error);
        alert('Erro ao mover o card');
    }
}

// Modal de Card
function openCardModal(columnId) {
    currentColumnId = columnId;
    currentCardId = null;
    document.getElementById('modalCardTitle').textContent = 'Criar Novo Card';
    inputCardTitle.value = '';
    inputCardDescription.value = '';
    modalCard.classList.add('active');
    inputCardTitle.focus();
}

function closeCardModal() {
    modalCard.classList.remove('active');
    currentCardId = null;
    currentColumnId = null;
}

async function saveCard() {
    const title = inputCardTitle.value.trim();
    const description = inputCardDescription.value.trim();

    if (!title) {
        alert('Por favor, digite um título para o card');
        return;
    }

    try {
        if (currentCardId) {
            // Editar card existente
            await fetch(`${CARDS_API_URL}/${currentCardId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ title, description })
            });
        } else {
            // Criar novo card - valida columnId antes de enviar
            if (currentColumnId == null) {
                alert('Coluna inválida. Abra o modal a partir do botão "+ Adicionar Card" na coluna desejada.');
                return;
            }

            const response = await fetch(CARDS_API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                // formato compatível com CreateCardRequest: { title, description, columnId }
                body: JSON.stringify({ title, description, columnId: currentColumnId })
            });

            if (!response.ok) {
                throw new Error('Erro ao criar card');
            }
        }

        closeCardModal();
        showBoardDetail(currentBoardId, boardTitle.textContent);
    } catch (error) {
        console.error('Erro ao salvar card:', error);
        alert('Erro ao salvar o card');
    }
}

async function deleteCard(cardId) {
    if (!confirm('Tem certeza que deseja deletar este card?')) {
        return;
    }

    try {
        await fetch(`${CARDS_API_URL}/${cardId}`, {
            method: 'DELETE'
        });

        showBoardDetail(currentBoardId, boardTitle.textContent);
    } catch (error) {
        console.error('Erro ao deletar card:', error);
        alert('Erro ao deletar o card');
    }
}

function showBoardsList() {
    boardDetail.style.display = 'none';
    boardsList.style.display = 'grid';
    currentBoardId = null;
    loadBoards();
}

async function createBoard() {
    const name = inputBoardName.value.trim();

    if (!name) {
        alert('Por favor, digite um nome para o board');
        return;
    }

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name })
        });

        if (response.ok) {
            closeModal();
            loadBoards();
        } else {
            alert('Erro ao criar board');
        }
    } catch (error) {
        console.error('Erro ao criar board:', error);
        alert('Erro ao criar board');
    }
}

async function deleteCurrentBoard() {
    if (!currentBoardId) return;

    if (!confirm('Tem certeza que deseja deletar este board?')) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${currentBoardId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showBoardsList();
        } else {
            alert('Erro ao deletar board');
        }
    } catch (error) {
        console.error('Erro ao deletar board:', error);
        alert('Erro ao deletar board');
    }
}

function openModal() {
    inputBoardName.value = '';
    modal.classList.add('active');
    inputBoardName.focus();
}

function closeModal() {
    modal.classList.remove('active');
}

// Enter para criar board
inputBoardName.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        createBoard();
    }
});

// Enter para criar card
inputCardTitle.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        saveCard();
    }
});

// Carregar boards ao iniciar
loadBoards();
