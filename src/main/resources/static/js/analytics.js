const originIds = ["sidebar.dashboard", "sidebar.recordHistory", "sidebar.budgets", "sidebar.analytics", "sidebar.profile",
    "analytics", "analytics.expenseOverview", "analytics.earningsOverview", "analytics.cashFlow", "analytics.outlook",
    "analytics.expenseLineChart", "analytics.earningsLineChart"]


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

function addActionToActivityLog(){
    const sessionActivityLog = sessionStorage.getItem('activityLog')
    const activityLog = JSON.parse(sessionActivityLog) || []

    const message = {page: `Analytics`, date: new Date()}
    activityLog.push(message)
    sessionStorage.setItem('activityLog', JSON.stringify(activityLog))
}

async function getUserRecordsOverview(token, startDate, endDate) {
    return (await fetch(`http://app.budgetmate.com/api/v1/analytics/overview/${startDate}/${endDate}`, {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
    })).json()
}

async function setUserOverviewDetails(token, startDate, endDate, dateOptions){
    const {monthlyExpenses, monthlyEarnings} = await getUserRecordsOverview(token, startDate.toISOString(), endDate.toISOString())

    const expenseOverview = document.getElementById("expenseOverview")
    const earningsOverview = document.getElementById("earningsOverview")
    const cashFlow = document.getElementById("cashFlow")
    const outlookProgressBar = document.getElementById("outlookProgressBar")
    const expensesLineChartDate = document.getElementById("expensesLineChartDate")
    const earningsLineChartDate = document.getElementById("earningsLineChartDate")
    const overviewDates = document.getElementById("overviewDates")

    const dateFields = [expensesLineChartDate, earningsLineChartDate, overviewDates]
    dateFields.forEach((item) => {
        item.textContent = `${startDate.toLocaleString('en-US', dateOptions)} - ${endDate.toLocaleString('en-US', dateOptions)}`
    })

    expenseOverview.textContent = monthlyExpenses;
    earningsOverview.textContent = monthlyEarnings;
    outlookProgressBar.style.width = `${Math.ceil(monthlyEarnings * 100 / (monthlyEarnings + monthlyExpenses))}%`
    cashFlow.textContent = (monthlyEarnings - monthlyExpenses).toString();
    cashFlow.style.color = monthlyEarnings - monthlyExpenses > 0 ? '#008000' : '#c80000'
}

async function setUserMonthOverviewListener(token, startDate, endDate, dateOptions){
    const {monthlyExpenses, monthlyEarnings} = await getUserRecordsOverview(token, startDate.toISOString(), endDate.toISOString())

    const expenseOverview = document.getElementById("expenseOverview")
    const earningsOverview = document.getElementById("earningsOverview")
    const cashFlow = document.getElementById("cashFlow")
    const outlookProgressBar = document.getElementById("outlookProgressBar")
    const overviewDates = document.getElementById("overviewDates")


    overviewDates.textContent =
        `${startDate.toLocaleString('en-US', dateOptions)} - ${endDate.toLocaleString('en-US', dateOptions)}`

    expenseOverview.textContent = monthlyExpenses;
    earningsOverview.textContent = monthlyEarnings;
    cashFlow.textContent = (monthlyEarnings - monthlyExpenses).toString();
    cashFlow.style.color = monthlyEarnings - monthlyExpenses > 0 ? '#008000' : '#c80000'

    outlookProgressBar.style.width = monthlyEarnings && monthlyExpenses
        ? `${Math.ceil(monthlyEarnings * 100 / (monthlyEarnings + monthlyExpenses))}%`
        : `0`
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

    const options = { year: 'numeric', month: 'short' };
    let startDate = new Date(new Date().getFullYear(), new Date().getMonth(), 1,0,0,0,0);
    let endDate = new Date(new Date().getFullYear(), new Date().getMonth() + 1, 1, 0,0,0,0);

    await setUserOverviewDetails(token, startDate, endDate, options)

    document.getElementById("previousMonthOverview").addEventListener('click', async () => {
        startDate = new Date(startDate.getFullYear(), startDate.getMonth() - 1);
        endDate = new Date(endDate.getFullYear(), endDate.getMonth() - 1);

        await setUserMonthOverviewListener(token, startDate, endDate, options)
    })

    document.getElementById("nextMonthOverview").addEventListener('click', async () => {
        startDate = new Date(startDate.getFullYear(), startDate.getMonth() + 1);
        endDate = new Date(endDate.getFullYear(), endDate.getMonth() + 1);

        await setUserMonthOverviewListener(token, startDate, endDate, options)
    })

    addActionToActivityLog()
    setUserActivityLogDetails()

    await renderGeneralUIByLanguage(token, lang)
    setEnglishLanguageSelectorListener(token)
    setDutchLanguageSelectorListener(token)
});