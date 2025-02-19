const originIds = ["sidebar.dashboard", "sidebar.recordHistory", "sidebar.budgets", "sidebar.analytics",
    "sidebar.profile", "record.date", "note","amount", "record.save", "record.deleteAccount"]

function setUserActivityLogDetails(){
    const sessionActivityLog = sessionStorage.getItem('activityLog')
    if(sessionActivityLog){
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

function addActionToActivityLog(recordId){
    const sessionActivityLog = sessionStorage.getItem('activityLog')
    const activityLog = JSON.parse(sessionActivityLog) || []

    const message = { page: `Record ${recordId} details`, date: new Date() }
    activityLog.push(message)
    sessionStorage.setItem('activityLog', JSON.stringify(activityLog))
}

async function getRecordCategories(token) {
    return (await fetch('http://app.budgetmate.com/api/v1/record/record-categories', {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
    })).json()
}

async function getUserRecord(token, id) {
   return (await fetch(`http://app.budgetmate.com/api/v1/record/${id}`, {
       method: 'GET',
       headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
   })).json()
}

async function updateUserRecord(token, payload) {
    return (await fetch(`http://app.budgetmate.com/api/v1/record/${id}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
        body: JSON.stringify(payload)
    })).json()
}

async function deleterUserRecord(token, id) {
    return (await fetch(`http://app.budgetmate.com/api/v1/record/${id}`, {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
    })).json()
}

async function getUserAccount(token, id) {
    return (await fetch(`http://app.budgetmate.com/api/v1/account/${id}`, {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
    })).json()
}

async function setRecordDetails(token, id){
    const record = await getUserRecord(token, id)
    const recordCategories = await getRecordCategories(token)

    const recordPaymentTime = new Date(record.paymentTime)
    const formattedPaymentTime = recordPaymentTime.toISOString().split(".")[0]
    const receivingAccount = record.receivingAccountId == null ? undefined : await getUserAccount(token, record.receivingAccountId)
    const withdrawalAccount = record.withdrawalAccountId == null ? undefined : await getUserAccount(token, record.withdrawalAccountId)

    const recordHeader = document.getElementById("recordHeader")
    const amount = document.getElementById("updateRecordAmount")
    const paymentTime = document.getElementById("updateRecordPaymentTime")
    const recordNote = document.getElementById("updateRecordNote")
    const accountField = document.getElementById("accountField")
    const accountField2 = document.getElementById("accountField-2")


    recordHeader.innerHTML = `<h3 class="mb-4 text-gray-800 alert-info border border-radius1 p-1 ml-1" id="recordType">${record.type}</h3>`
    if(record.type.toUpperCase() !== 'TRANSFER') {
        recordHeader.innerHTML +=
            `<h3 class="mb-4 text-gray-800 alert-info border border-radius1 p-1 ml-1" id="recordCategory">${record.category.name}</h3>`
    }

    amount.textContent = record.amount
    paymentTime.value = formattedPaymentTime
    recordNote.value = record.note

    if(receivingAccount && withdrawalAccount){
        renderRecordAccounts(accountField, receivingAccount, 'accountField-Input', 'Receiving Account')
        renderRecordAccounts(accountField2, withdrawalAccount, 'accountField-2-Input', 'Withdrawal Account')
    }else if(receivingAccount == null && withdrawalAccount){
        renderRecordCategoriesDropdown(accountField, recordCategories, 'accountField-Input', record.category)
        renderRecordAccounts(accountField2, withdrawalAccount, 'accountField-2-Input', 'Withdrawal Account')
    }else if(receivingAccount){
        renderRecordCategoriesDropdown(accountField, recordCategories, 'accountField-Input', record.category)
        renderRecordAccounts(accountField2, receivingAccount, 'accountField-2-Input', 'Receiving Account')
    }

    renderAccountCards([receivingAccount, withdrawalAccount])
}

function setSubmitRecordListener(token, id){
    document.getElementById("updateRecordForm").addEventListener('submit', async (event) => {
        event.preventDefault()

        const submitButton = document.getElementById("submitRecordButton")
        const {value: recordPaymentTimeValue } = document.getElementById("updateRecordPaymentTime")
        const {value: recordNoteValue} = document.getElementById("updateRecordNote")
        const recordCategory = document.getElementById("accountField-Input")

        const payload = {
            paymentTime: recordPaymentTimeValue, note: recordNoteValue,
            ...recordCategory.tagName === 'SELECT' && { category: recordCategory.value }
        }

        await updateUserRecord(token, payload)

        await setRecordDetails(token, id)
        submitButton.blur()
    })
}

function setDeleteRecordListener(token, id){
    document.getElementById("deleteRecordButton").addEventListener("click", async () => {
        await deleterUserRecord(token, id)

        window.location.href = "/records-history"
    })
}

function renderRecordAccounts(accountContainer, account, id, label){
    accountContainer.innerHTML =
        `
            <label for="${id}" class="d-flex flex-row align-items-start" id="accountField-label">
              ${label}
            </label>
            <div id="${id}" class="form-control">${account.name}</div>
        `
}

function renderRecordCategoriesDropdown(dropdownContainer, recordCategories, id, recordCategory){
    const categoriesHTML = recordCategories.map(category =>
        category.id === recordCategory.id
            ? `<option selected name="${category.name}" value="${category.name}" id="${category.id}">"${category.name}"</option>`
            : `<option name="${category.name}" value="${category.name}" id="${category.id}">"${category.name}"</option>`
    ).join('');

    dropdownContainer.innerHTML =
        `
        <label for="${id}" class="d-flex flex-row align-items-start" id="accountField-label">
               <i class="fas fa-asterisk fa-xs" style="color: #f22626; font-size: 6px;"></i>
               Record Categories
        </label>
        <select name="${id}" id="${id}" class="form-control">
        ${categoriesHTML}
        </select>
        `
}

function renderAccountCards(accounts){
    const budgetsAccountContainer = document.getElementById("budgetsAccountContainer")

    accounts.forEach((account) => {
        if(account){
            const accountColor = account.avatarColor

            budgetsAccountContainer.innerHTML +=
                `<div class="col-xl-3 col-md-6 mb-4">
                        <div class="card h-100 py-2" style="border-left: 0.25rem solid ${accountColor}">
                            <div class="card-body">
                                <a target="_blank" class="text-decoration-none" 
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
        </div>`
        }
    })
}

async function renderGeneralUIByLanguage(token, lang){
    const {translations} = await getIdsTranslation(token, lang, { originIds })

    Object.keys(translations).forEach((id) => {
        if(document.getElementsByClassName(id)){
            Array.from(document.getElementsByClassName(id))
                .forEach((item) => item.textContent = translations[id])
        }
    })
}

function setEnglishLanguageSelectorListener(token){
    document.getElementById("lang-en").addEventListener("click", async () => {
        sessionStorage.setItem('lang', 'en')
        await renderGeneralUIByLanguage(token, 'en')
    })
}

function setDutchLanguageSelectorListener(token){
    document.getElementById("lang-nl").addEventListener("click", async () => {
        sessionStorage.setItem('lang', 'nl')
        await renderGeneralUIByLanguage(token, 'nl')
    })
}

document.addEventListener('DOMContentLoaded', async function () {
    const token = localStorage.getItem('token')
    const lang = sessionStorage.getItem('lang')
    const recordId = window.location.pathname.split("/").pop()

    await setRecordDetails(token, recordId)
    setSubmitRecordListener(token, recordId)
    setDeleteRecordListener(token, recordId)

    addActionToActivityLog(recordId)
    setUserActivityLogDetails()

    await renderGeneralUIByLanguage(token, lang)
    setEnglishLanguageSelectorListener(token)
    setDutchLanguageSelectorListener(token)
});