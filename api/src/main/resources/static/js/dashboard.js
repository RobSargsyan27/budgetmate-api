const originIds = ["sidebar.dashboard", "sidebar.recordHistory", "sidebar.budgets", "sidebar.analytics",
    "sidebar.profile", "dashboard.action", "dashboard.action.addRecord", "dashboard.action.addBudget",
    "dashboard.action.addAccount", "dashboard.accounts", "dashboard.overview", "dashboard.overview.monthlyEarnings",
    "dashboard.overview.monthlyExpenses", "dashboard.overview.annualEarnings", "dashboard.overview.monthlyOutlook",
    "dashboard.overview.monthlyExpensesOverview", "dashboard.overview.topExpenseCategories",
    "dashboard.addRecordModal.addRecord", "dashboard.addRecordModal.expense", "dashboard.addRecordModal.income",
    "dashboard.addRecordModal.transfer", "dashboard.addAccountModal.addAccount", "dashboard.addAccountModal.newAccount",
    "dashboard.addAccountModal.existingAccount", "record.date", "record.category", "receivingAccount",
    "withdrawalAccount", "name", "currency", "amount", "modal.close", "note", "budget.category",
    "account.currentBalance", "account.accountType", "account.accountColor", "account.ownerUsername",
    "logout.warning.header", "logout.warning", "logout.cancel", "logout.button"]

function setUserActivityLogDetails() {
    const sessionActivityLog = sessionStorage.getItem('activityLog')
    if (sessionActivityLog) {
        const activityLog = JSON.parse(sessionActivityLog)
        const activityLogTable = document.getElementById("activityLogTable")

        activityLogTable.innerHTML = ''
        activityLog.forEach((log) => {
            activityLogTable.innerHTML +=
                `<tr>
                <td>${log.page}</td>
                <td>${log.date}</td>
            </tr> `
        })
    }
}

function addActionToActivityLog() {
    const sessionActivityLog = sessionStorage.getItem('activityLog')
    const activityLog = JSON.parse(sessionActivityLog) || []

    const message = {page: `Dashboard`, date: new Date()}
    activityLog.push(message)
    sessionStorage.setItem('activityLog', JSON.stringify(activityLog))
}

async function getUserDetails(token) {
    try {
        const response = await fetch('http://app.budgetmate.com/api/v1/user', {
            method: 'GET',
            headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
        })

        return response.json();
    } catch (e) {
        window.location.href = "/500-error"
    }
}

async function getUserDashboardAnalytics(token) {
    try {
        const response = await fetch('api/v1/analytics/dashboard', {
            method: 'GET',
            headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
        })

        return response.json();
    } catch (e) {
        window.location.href = "/500-error"
    }
}

async function getRecordCategories(token) {
    try {
        const response = await fetch('api/v1/record/record-categories', {
            method: 'GET',
            headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
        })

        return response.json();
    } catch (e) {
        window.location.href = "/500-error"
    }
}

async function getUserAccounts(token) {
    try {
        const response = await fetch('api/v1/account', {
            method: 'GET',
            headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
        })


        return response.json();
    } catch (e) {
        window.location.href = "/500-error"
    }
}

async function setRecordCategories(token) {
    const incomeRecordCategoryDropdown = document.getElementById("incomeRecordCategoryDropdown")
    const expenseRecordCategoryDropdown = document.getElementById("expenseRecordCategoryDropdown")
    const budgetRecordCategoriesDropdown = document.getElementById("budgetRecordCategoriesDropdown")
    const recordCategories = await getRecordCategories(token);

    recordCategories.forEach((recordCategories) => {
        incomeRecordCategoryDropdown.innerHTML +=
            `<option name="${recordCategories.name}" value="${recordCategories.name}" id="${recordCategories.id}">"${recordCategories.name}"</option>`

        expenseRecordCategoryDropdown.innerHTML +=
            `<option name="${recordCategories.name}" value="${recordCategories.name}" id="${recordCategories.id}">"${recordCategories.name}"</option>`

        budgetRecordCategoriesDropdown.innerHTML +=
            `<div class="form-check">
                    <input type="checkbox" class="form-check-input" name="${recordCategories.name}" value="${recordCategories.name}"/>
                    <label class="mr-2">${recordCategories.name}</label>
            </div>`
    })
}

async function setUserAccounts(token) {
    const incomeReceivingAccountDropdown = document.getElementById("incomeReceivingAccountDropdown");
    const expenseWithdrawalAccountDropdown = document.getElementById("expenseWithdrawalAccountDropdown")
    const transferReceivingAccountDropdown = document.getElementById("transferReceivingAccountDropdown")
    const transferWithdrawalAccountDropdown = document.getElementById("transferWithdrawalAccountDropdown")
    const accounts = await getUserAccounts(token)

    accounts.forEach((account) => {
        incomeReceivingAccountDropdown.innerHTML +=
            `<option name="${account.name}" value="${account.id}" id="${account.id}">"${account.name}"</option>`

        expenseWithdrawalAccountDropdown.innerHTML +=
            `<option name="${account.name}" value="${account.id}" id="${account.id}">"${account.name}"</option>`

        transferReceivingAccountDropdown.innerHTML +=
            `<option name="${account.name}" value="${account.id}" id="${account.id}">"${account.name}"</option>`

        transferWithdrawalAccountDropdown.innerHTML +=
            `<option name="${account.name}" value="${account.id}" id="${account.id}">"${account.name}"</option>`
    })
}

async function setDashboardUserAccounts(token) {
    const dashboardAccounts = document.getElementById("dashboardAccounts")
    const accounts = await getUserAccounts(token)

    dashboardAccounts.innerHTML = ""
    accounts.forEach((account) => {
        const accountColor = account.avatarColor

        dashboardAccounts.innerHTML += `
        <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card h-100 py-2" style="border-left: 0.25rem solid ${accountColor}">
                            <div class="card-body">
                                <a class="text-decoration-none" 
                                href="/account/${account.id}">
                                  <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-lg font-weight-bold text-uppercase mb-1" style="color: ${accountColor}">${account.name}</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">${account.currentBalance}</div>
                                        <div class="h6 mb-0 font-weight-light text-gray-900">${account.currency}</div>
                                    </div>
                                    <div class="col-auto"><i class="fas fa-landmark fa-2x" style="color: ${accountColor}"></i></div>
                                  </div>
                                </a>
                            </div>
                        </div>
        </div>
        `
    })
}

async function setDashboardUserAnalytics(token) {
    const {monthlyEarnings, monthlyExpenses, annualEarnings} = await getUserDashboardAnalytics(token);

    const monthlyEarningsStat = document.getElementById("monthlyEarningsStat")
    const annualEarningsStat = document.getElementById("annualEarningsStat")
    const monthlyExpensesStat = document.getElementById("monthlyExpensesStat")
    const cashFlowStat = document.getElementById("cashFlowStat")

    monthlyEarningsStat.textContent = monthlyEarnings
    monthlyExpensesStat.textContent = monthlyExpenses
    annualEarningsStat.textContent = annualEarnings

    const cashFlowIncome = monthlyEarnings * 100 / (monthlyEarnings + monthlyExpenses)

    cashFlowStat.innerHTML =
        `
        <div class="row no-gutters align-items-center">
                    <div class="col">
                        <div class="progress-oppose progress-lg">
                            <div class="progress-bar bg-success" role="progressbar" 
                                 style="width: ${cashFlowIncome.toFixed(2)}%" aria-valuemin="0" 
                                 aria-valuemax="100"></div>
                        </div>
                    </div>
        </div>
        `
}

function submitRecord(token, userId) {
    document.getElementById('addRecordButton').addEventListener('click', async function () {
        const activeTab = document.querySelector('#recordTabs .nav-link.active').textContent;

        switch (activeTab) {
            case 'Expense':
                await submitExpenseRecord(token, userId)
                break;
            case 'Income':
                await submitIncomeRecord(token, userId)
                break;
            case 'Transfer':
                await submitTransferRecord(token, userId)
                break;
        }

        await setDashboardUserAccounts(token)
        await setDashboardUserAnalytics(token)
        document.getElementById('closeRecordModalButton').click();
    })
}

async function submitExpenseRecord(token, userId) {
    const amountInput = document.getElementById("expenseAmount");
    const paymentTimeInput = document.getElementById("expensePaymentTime");
    const categoryDropdown = document.getElementById("expenseRecordCategoryDropdown");
    const withdrawalAccountDropdown = document.getElementById("expenseWithdrawalAccountDropdown");
    const noteInput = document.getElementById("expenseNote");

    const amount = amountInput.value;
    const paymentTime = paymentTimeInput.value;
    const category = categoryDropdown.value;
    const withdrawalAccountId = withdrawalAccountDropdown.value;
    const note = noteInput.value;

    try{
        const response = await fetch('api/v1/record/expense', {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
            body: JSON.stringify({userId, amount, paymentTime, category, withdrawalAccountId, note})
        });
    }catch (e){
        window.location.href = "/500-error"
    }



    amountInput.value = "";
    paymentTimeInput.value = "";
    categoryDropdown.selectedIndex = 0;
    withdrawalAccountDropdown.selectedIndex = 0;
    noteInput.value = "";
}

async function submitIncomeRecord(token, userId) {
    const amountInput = document.getElementById("incomeAmount");
    const paymentTimeInput = document.getElementById("incomePaymentTime");
    const categoryDropdown = document.getElementById("incomeRecordCategoryDropdown");
    const receivingAccountDropdown = document.getElementById("incomeReceivingAccountDropdown");
    const noteInput = document.getElementById("incomeNote");

    const amount = amountInput.value;
    const paymentTime = paymentTimeInput.value;
    const category = categoryDropdown.value;
    const receivingAccountId = receivingAccountDropdown.value;
    const note = noteInput.value;

    try{
        const response = await fetch('api/v1/record/income', {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
            body: JSON.stringify({userId, amount, paymentTime, category, receivingAccountId, note})
        });
    }catch (e){
        window.location.href = "/500-error"
    }

    amountInput.value = "";
    paymentTimeInput.value = "";
    categoryDropdown.selectedIndex = 0;
    receivingAccountDropdown.selectedIndex = 0;
    noteInput.value = "";
}


async function submitTransferRecord(token, userId) {
    const amountInput = document.getElementById("transferAmount");
    const paymentTimeInput = document.getElementById("transferPaymentTime");
    const receivingAccountDropdown = document.getElementById("transferReceivingAccountDropdown");
    const withdrawalAccountDropdown = document.getElementById("transferWithdrawalAccountDropdown");
    const noteInput = document.getElementById("transferNote");

    const amount = amountInput.value;
    const paymentTime = paymentTimeInput.value;
    const receivingAccountId = receivingAccountDropdown.value;
    const withdrawalAccountId = withdrawalAccountDropdown.value;
    const note = noteInput.value;

    try{
        const response = await fetch('api/v1/record/transfer', {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
            body: JSON.stringify({userId, amount, paymentTime, withdrawalAccountId, receivingAccountId, note})
        });
    }catch (e){
        window.location.href = "/500-error"
    }

    amountInput.value = "";
    paymentTimeInput.value = "";
    receivingAccountDropdown.selectedIndex = 0;
    withdrawalAccountDropdown.selectedIndex = 0;
    noteInput.value = "";
}


function submitBudget(token, userId) {
    document.getElementById('addBudgetButton').addEventListener('click', async function () {
        const nameInput = document.getElementById("budgetName");
        const amountInput = document.getElementById("budgetAmount");
        const budgetCategoriesCheckboxes = document.querySelectorAll('#budgetRecordCategoriesDropdown .form-check-input');

        const name = nameInput.value;
        const amount = amountInput.value;
        const budgetCategories = Array
            .from(document.querySelectorAll('#budgetRecordCategoriesDropdown .form-check-input:checked'))
            .map(input => input.value);

        try{
            const response = await fetch('api/v1/budget', {
                method: 'POST',
                headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
                body: JSON.stringify({userId, amount, name, budgetCategories})
            });
        }catch (e){
            window.location.href = "/500-error"
        }

        nameInput.value = "";
        amountInput.value = "";
        budgetCategoriesCheckboxes.forEach(checkbox => {
            checkbox.checked = false;
        });

        document.getElementById('closeBudgetModalButton').click();
    })
}

function submitAccount(token, userId) {
    document.getElementById('addAccountButton').addEventListener('click', async function () {
        const activeTab = document.querySelector('#accountTabs .nav-link.active').textContent;

        switch (activeTab) {
            case 'New Account':
                await submitNewAccount(token, userId)
                break;
            case 'Existing Account':
                await submitExistingAccountRequest(token, userId)
                break;
        }

        await setDashboardUserAccounts(token)
        await setDashboardUserAnalytics(token)
        document.getElementById('closeAccountModalButton').click();
    })
}

async function submitNewAccount(token, userId) {
    const name = document.getElementById("accountName")
    const currency = document.getElementById("accountCurrency")
    const currentBalance = document.getElementById("accountCurrentBalance")
    const type = document.getElementById("accountType")
    const avatarColor = document.getElementById("accountAvatarColor")

    const nameValue = name.value
    const currencyValue = currency.value
    const currentBalanceValue = currentBalance.value
    const typeValue = type.value
    const avatarColorValue = avatarColor.value

try{
    const response = await fetch('api/v1/account', {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
        body: JSON.stringify({
            userId,
            name: nameValue,
            currency: currencyValue,
            currentBalance: currentBalanceValue,
            type: typeValue,
            avatarColor: avatarColorValue
        })
    });
} catch (e) {
    window.location.href = "/500-error"
}

    name.value = ""
    currency.selectedIndex = 0
    currentBalance.value = 0
    type.selectedIndex = 0
    avatarColor.value = "#000053"
}

async function submitExistingAccountRequest(token, userId) {
    const accountName = document.getElementById("existingAccountName")
    const ownerUsername = document.getElementById("existingAccountOwnerUsername")

    const accountNameValue = accountName.value
    const ownerUsernameValue = ownerUsername.value

try{
    const response = await fetch('api/v1/account/existing', {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
        body: JSON.stringify({userId, accountName: accountNameValue, ownerUsername: ownerUsernameValue})
    });
}catch (e) {
    window.location.href = "/500-error"
}

    accountName.value = ""
    ownerUsername.value = ""
}

async function renderGeneralUIByLanguage(token, lang) {
    const {translations} = await getIdsTranslation(token, lang, {originIds})

    Object.keys(translations).forEach((id) => {
        if (document.getElementsByClassName(id)) {
            console.log(document.getElementsByClassName(id))
            Array.from(document.getElementsByClassName(id))
                .forEach((item) => item.textContent = translations[id])
        }
    })
}

function setEnglishLanguageSelectorListener(token) {
    document.getElementById("lang-en").addEventListener("click", async () => {
        sessionStorage.setItem('lang', 'en')
        await renderGeneralUIByLanguage(token, 'en')
    })
}

function setDutchLanguageSelectorListener(token) {
    document.getElementById("lang-nl").addEventListener("click", async () => {
        sessionStorage.setItem('lang', 'nl')
        await renderGeneralUIByLanguage(token, 'nl')
    })
}

document.addEventListener('DOMContentLoaded', async function () {
    const token = localStorage.getItem('token')
    const lang = sessionStorage.getItem('lang')
    const user = await getUserDetails(token)

    await setDashboardUserAccounts(token)
    await setRecordCategories(token)
    await setUserAccounts(token)
    await setDashboardUserAnalytics(token)

    submitRecord(token, user.id)
    submitBudget(token, user.id)
    submitAccount(token, user.id)

    addActionToActivityLog()
    setUserActivityLogDetails()

    await renderGeneralUIByLanguage(token, lang)
    setEnglishLanguageSelectorListener(token)
    setDutchLanguageSelectorListener(token)
});
