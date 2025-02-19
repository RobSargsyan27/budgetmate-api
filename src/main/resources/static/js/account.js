const originIds = ["sidebar.dashboard", "sidebar.recordHistory", "sidebar.budgets", "sidebar.analytics",
    "sidebar.profile", "name", "account.currentBalance", "currency", "account.accountType", "account.accountColor",
    "account.save", "deleteAccount"]

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

function addActionToActivityLog(accountId) {
    const sessionActivityLog = sessionStorage.getItem('activityLog')
    const activityLog = JSON.parse(sessionActivityLog) || []

    const message = {page: `Account ${accountId} details`, date: new Date()}
    activityLog.push(message)
    sessionStorage.setItem('activityLog', JSON.stringify(activityLog))
}

async function getUserAccount(token, id) {
    return (await fetch(`http://app.budgetmate.com/api/v1/account/${id}`, {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
    })).json()
}

async function deleteUserAccount(token, id) {
    await fetch(`http://app.budgetmate.com/api/v1/account/${id}`, {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
    });
}

async function setAccountDetails(token, id) {
    const account = await getUserAccount(token, id)

    const accountCard = document.getElementById("accountCard")
    const accountName = document.getElementById("accountName")
    const updateAccountName = document.getElementById("updateAccountName")
    const updateAccountCurrentBalance = document.getElementById("updateAccountCurrentBalance")
    const updateAccountCurrency = document.getElementById("updateAccountCurrency")
    const updateAccountType = document.getElementById("updateAccountType")
    const updateAccountAvatarColor = document.getElementById("updateAccountAvatarColor")

    accountCard.style.borderBottom = `0.25rem solid ${account.avatarColor}`;
    accountName.textContent = account.name
    updateAccountName.value = account.name
    updateAccountCurrentBalance.value = account.currentBalance
    updateAccountCurrency.textContent = account.currency
    updateAccountType.value = account.type
    updateAccountAvatarColor.value = account.avatarColor

}

function setSubmitAccountListener(token, id) {
    document.getElementById("updateAccountForm").addEventListener("submit", async (event) => {
        event.preventDefault()

        const submitButton = document.getElementById("submitAccountButton")
        const name = document.getElementById("updateAccountName").value
        const currentBalance = document.getElementById("updateAccountCurrentBalance").value
        const type = document.getElementById("updateAccountType").value
        const avatarColor = document.getElementById("updateAccountAvatarColor").value

        await fetch(`http://app.budgetmate.com/api/v1/account/${id}`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
            body: JSON.stringify({name, currentBalance, type, avatarColor})
        })

        await setAccountDetails(token, id)
        submitButton.blur()
    })
}

function setDeleteAccountListener(token, id) {
    document.getElementById("deleteAccountButton").addEventListener("click", async () => {
        await deleteUserAccount(token, id)

        window.location.href = "/dashboard"
    })
}

async function renderGeneralUIByLanguage(token, lang) {
    const {translations} = await getIdsTranslation(token, lang, {originIds})

    Object.keys(translations).forEach((id) => {
        document.getElementsByClassName(id) && Array.from(document.getElementsByClassName(id))
            .forEach((item) => item.textContent = translations[id])
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
    const accountId = window.location.pathname.split("/").pop()

    await setAccountDetails(token, accountId)
    setSubmitAccountListener(token, accountId)
    setDeleteAccountListener(token, accountId)

    addActionToActivityLog(accountId)
    setUserActivityLogDetails()

    await renderGeneralUIByLanguage(token, lang)
    setEnglishLanguageSelectorListener(token)
    setDutchLanguageSelectorListener(token)
});