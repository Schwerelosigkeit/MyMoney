let currentType = '';
let selectedCategory = null;
let categoriesMap = {};

async function loadBalance() {
    const response = await fetch('/api/balance');
    const balance = await response.json();
    document.getElementById('balance-value').textContent = balance.toFixed(2);
}

async function loadTransactions() {
    const response = await fetch('/api/monthly-transactions');
    const transactions = await response.json();
    const list = document.getElementById('transactions-list');
    list.innerHTML = '';
    transactions.forEach(t => {
        const li = document.createElement('li');
        li.classList.add('transaction-item');

        const headerDiv = document.createElement('div');
        headerDiv.classList.add('transaction-header');

        const typeAndCatDiv = document.createElement('div');
        typeAndCatDiv.classList.add('type-and-category');

        const typeSpan = document.createElement('span');
        typeSpan.classList.add('transaction-type');
        typeSpan.textContent = t.type === 'INCOME' ? 'Доход' : 'Расход';

        typeAndCatDiv.appendChild(typeSpan);

        if (t.type === 'EXPENSE' && t.category) {
            const catSpan = document.createElement('span');
            catSpan.classList.add('transaction-category');
            catSpan.style.backgroundColor = t.color;
            catSpan.textContent = t.category;
            typeAndCatDiv.appendChild(catSpan);
        }

        const amountSpan = document.createElement('span');
        amountSpan.classList.add('transaction-amount', t.type.toLowerCase());
        amountSpan.textContent = (t.type === 'INCOME' ? '+' : '-') + t.amount.toFixed(2);

        const dateSpan = document.createElement('span');
        dateSpan.classList.add('transaction-date');
        dateSpan.textContent = new Date(t.date).toLocaleString();

        headerDiv.appendChild(typeAndCatDiv);
        headerDiv.appendChild(amountSpan);
        headerDiv.appendChild(dateSpan);

        const deleteBtn = document.createElement('span');
        deleteBtn.classList.add('delete-btn');
        deleteBtn.textContent = '×';
        deleteBtn.onclick = async () => {
            if (confirm('Вы уверены, что хотите удалить эту транзакцию?')) {
                try {
                    const response = await fetch(`/api/transactions/${t.id}`, {
                        method: 'DELETE'
                    });
                    if (!response.ok) {
                        throw new Error('Ошибка удаления');
                    }
                    loadBalance();
                    loadTransactions();
                    loadMonthExpenses();
                } catch (error) {
                    alert('Ошибка: ' + error.message);
                }
            }
        };
        headerDiv.appendChild(deleteBtn);

        li.appendChild(headerDiv);

        if (t.description) {
            const descDiv = document.createElement('div');
            descDiv.classList.add('transaction-description');
            descDiv.textContent = t.description;
            li.appendChild(descDiv);
        }

        list.appendChild(li);
    });
}

async function loadCategories() {
    const response = await fetch('/api/categories');
    const categories = await response.json();
    const catList = document.getElementById('categories-list');
    catList.innerHTML = '';
    categoriesMap = {};
    categories.forEach(cat => {
            categoriesMap[cat.value] = { name: cat.name, color: cat.color };
            const btn = document.createElement('button');
            btn.classList.add('category-btn');
            btn.style.backgroundColor = cat.color;
            btn.textContent = cat.name;
            btn.onclick = () => selectCategory(btn, cat.value);
            catList.appendChild(btn);
        });
}

function selectCategory(btn, catValue) {
    document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('selected'));
    btn.classList.add('selected');
    selectedCategory = catValue;
}

async function submitTransaction() {
    const amountInput = document.getElementById('amount');
    const descriptionInput = document.getElementById('description');
    const amount = parseFloat(amountInput.value);
    const description = descriptionInput.value.trim();

    if (isNaN(amount) || amount <= 0) {
        alert('Введите положительную сумму (минимум 0.01).');
        amountInput.focus();
        return;
    }
    if (!/^\d+(\.\d{1,2})?$/.test(amountInput.value)) {
        alert('Сумма должна быть числом с максимум 2 знаками после запятой.');
        amountInput.focus();
        return;
    }

    if (description && !/^[а-яА-Яa-zA-Z0-9\s.,?!-]*$/.test(description)) {
        alert('Комментарий может содержать только буквы, цифры, пробелы и простую пунктуацию (.,?!-). Удалите специальные символы.');
        descriptionInput.focus();
        return;
    }

    if (currentType === 'expense' && !selectedCategory) {
        alert('Выберите категорию для расхода.');
        return;
    }

    let url = currentType === 'income' ? '/api/income' : '/api/expense';
    let body = currentType === 'income'
        ? { amount, description }
        : { amount, description, category: selectedCategory};


    const response = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });

    closeModal();
    loadBalance();
    loadTransactions();
    loadMonthExpenses();
}

function openModal(type) {
    currentType = type;
    const modal = document.getElementById('transaction-modal');
    const title = document.getElementById('modal-title');
    const catGroup = document.getElementById('categories-group');

    title.textContent = type === 'income' ? 'Добавить доход' : 'Добавить расход';
    catGroup.style.display = type === 'expense' ? 'block' : 'none';
    if (type === 'expense') {
        loadCategories();
    }
    selectedCategory = null;
    document.getElementById('amount').value = '';
    document.getElementById('description').value = '';
    modal.style.display = 'flex';
}

function closeModal() {
    document.getElementById('transaction-modal').style.display = 'none';
}

async function loadMonthExpenses() {
    const response = await fetch('/api/transactions/month');
    const monthData = await response.json();

    const currentMonth = new Date().toLocaleString('ru-RU', { month: 'long' });
    document.getElementById('month-expenses-title').textContent = `Сумма расходов за ${currentMonth}: ${monthData.totalExpenses.toFixed(2)} руб.`;

    const categoryKeys = Object.keys(monthData.expensesByCategory).sort((a, b) => monthData.expensesByCategory[b] - monthData.expensesByCategory[a]);
    const labels = categoryKeys.map(key => categoriesMap[key] ? categoriesMap[key].name : key);
    const amounts = categoryKeys.map(key => monthData.expensesByCategory[key]);
    const colors = categoryKeys.map(key => categoriesMap[key] ? categoriesMap[key].color : '#999');

    const ctx = document.getElementById('expenses-chart').getContext('2d');
    if (window.expensesChart) {
        window.expensesChart.destroy();
    }
    window.expensesChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                data: amounts,
                backgroundColor: colors,
                borderColor: '#1c1c1c',
                borderWidth: 3
            }]
        },
        options: {
            responsive: false,
            plugins: {
                legend: {
                    display: false
                },
                datalabels: {
                    color: '#fff',
                    formatter: (value, context) => {
                        const total = context.dataset.data.reduce((acc, val) => acc + val, 0);
                        const percentage = ((value / total) * 100).toFixed(1);
                        return percentage > 5 ? percentage + '%' : '';
                    },
                    font: {
                        weight: 'bold',
                        size: 14
                    },
                    anchor: 'center',
                    align: 'end'
                }
            }
        },
        plugins: [ChartDataLabels]
    });

    const mid = Math.ceil(categoryKeys.length / 2);
    const column1Keys = categoryKeys.slice(0, mid);
    const column2Keys = categoryKeys.slice(mid);

    const column1 = document.getElementById('categories-column1');
    column1.innerHTML = '';
    column1Keys.forEach(key => {
        const li = document.createElement('li');
        const span = document.createElement('span');
        span.classList.add('category-summary-item');
        span.style.backgroundColor = categoriesMap[key].color;
        span.textContent = `${categoriesMap[key].name}: ${monthData.expensesByCategory[key].toFixed(2)} руб.`;
        li.appendChild(span);
        column1.appendChild(li);
    });

    const column2 = document.getElementById('categories-column2');
    column2.innerHTML = '';
    column2Keys.forEach(key => {
        const li = document.createElement('li');
        const span = document.createElement('span');
        span.classList.add('category-summary-item');
        span.style.backgroundColor = categoriesMap[key].color;
        span.textContent = `${categoriesMap[key].name}: ${monthData.expensesByCategory[key].toFixed(2)} руб.`;
        li.appendChild(span);
        column2.appendChild(li);
    });
}

function toggleStats() {
    const rightSection = document.querySelector('.right-section');
    const toggleBtn = document.getElementById('toggle-stats-btn');

    const computedDisplay = window.getComputedStyle(rightSection).display;

    if (computedDisplay === 'block') {
        rightSection.style.display = 'none';
        toggleBtn.textContent = 'Открыть статистику расходов за текущий месяц';
    } else {
        rightSection.style.display = 'block';
        toggleBtn.textContent = 'Скрыть расходы за текущий месяц';
    }
}

window.onload = () => {
    loadBalance();
    loadTransactions();
    loadCategories();
    loadMonthExpenses();

    document.getElementById('toggle-stats-btn').addEventListener('click', toggleStats);

};