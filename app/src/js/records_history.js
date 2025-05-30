function setUserActivityLogDetails(){
  const sessionActivityLog = sessionStorage.getItem('activityLog');
  if(sessionActivityLog){
    const activityLog = JSON.parse(sessionActivityLog);
    const activityLogTable = document.getElementById('activityLogTable');

    activityLogTable.innerHTML = '';
    activityLog.forEach((log) => {
      activityLogTable.innerHTML +=
                `<tr>
                <td>${log.page}</td>
                <td>${log.date}</td>
            </tr> `;
    });
  }
}

function addActionToActivityLog(){
  const sessionActivityLog = sessionStorage.getItem('activityLog');
  const activityLog = JSON.parse(sessionActivityLog) || [];

  const message = { page: 'Records History', date: new Date() };
  activityLog.push(message);
  sessionStorage.setItem('activityLog', JSON.stringify(activityLog));
}

async function getRecordReportUrl(token){
  const body = getRecordFilters();

  try{
    const response = await fetch('api/v1/record/report', {
      method: 'POST',
      headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
      body: JSON.stringify(body)
    });

    return response.blob();
  }catch (e) {
    window.location.href = '/500-error';
  }

}

function getRecordFilters() {
  const recordTypeSelect = document.getElementById('recordTypeSelect').value;
  const timestampStartFilter = document.getElementById('timestampStartFilter').value;
  const timestampEndFilter = document.getElementById('timestampEndFilter').value;
  const amountStartFilter = document.getElementById('amountStartFilter').value;
  const amountEndFilter = document.getElementById('amountEndFilter').value;

  return {
    recordType: recordTypeSelect === 'all' ? null : recordTypeSelect,
    amountGreaterThan: amountStartFilter || null,
    amountLessThan: amountEndFilter || null,
    paymentTimeGreaterThan: timestampStartFilter === null ? null : new Date(timestampStartFilter),
    paymentTimeLessThan: timestampEndFilter === null ? null : new Date(timestampEndFilter)
  };
}

async function getUserRecordsCount(token) {
  const body = getRecordFilters();

  try{
    const response = await fetch('api/v1/record/count', {
      method: 'POST',
      headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
      body: JSON.stringify(body)
    });

    return response.json();
  }catch (e) {
    window.location.href = '/500-error';
  }
}

async function getUserRecords(token, limit, offset) {
  const body = getRecordFilters();

  try{
    const result = await fetch(`api/v1/record/${limit}/${offset}`, {
      method: 'POST',
      headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
      body: JSON.stringify(body)
    });

    return result.json();
  }catch (e) {
    window.location.href = '/500-error';
  }
}

function setRowLinkListeners(){
  document.querySelectorAll('.clickable-row').forEach(row => {
    row.addEventListener('click', function () {
      window.location.href = this.dataset.href;
    });
  });
}

async function renderRecordTable(token, currentPage) {
  const limit = document.getElementById('recordsPerPageSelect').value;
  const offset = (currentPage - 1) * parseInt(limit);
  const recordsTableBody = document.getElementById('recordsHistoryTableBody');
  const records = await getUserRecords(token, limit, offset);

  recordsTableBody.innerHTML = '';
  records.forEach((record) => {
    const _paymentTime = new Date(record.paymentTime);
    recordsTableBody.innerHTML +=
            `<tr class="clickable-row" data-href="http://app.budgetmate.com/record/${record.id}">
                <td>${record.amount}</td>
                <td>${record.type}</td>
                <td>${record.category?.name || ''}</td>
                <td>${record.currency}</td>
                <td>${_paymentTime.getMonth() + 1}/${_paymentTime.getDate()}/${_paymentTime.getFullYear()}</td>
                <td>${record.receivingAccountName || ''}</td>
                <td>${record.withdrawalAccountName || ''}</td>
             </tr>`;
  });
}

function renderPagination(recordsCount, recordsPerPage, currentPage, token) {
  const paginationControls = document.getElementById('paginationControls');
  const totalPages = Math.ceil(recordsCount / recordsPerPage);
  paginationControls.innerHTML = '';

  for (let i = 1; i <= totalPages; i++) {
    const li = document.createElement('li');
    li.className = `page-item ${i === currentPage ? 'active' : ''}`;
    li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
    li.addEventListener('click', async function (e) {
      e.preventDefault();
      currentPage = i;
      await renderRecordTable(token, currentPage);
      renderPagination(recordsCount, recordsPerPage, currentPage, token);
      setRowLinkListeners();
    });
    paginationControls.appendChild(li);
  }
}

function setGenerateReportListener(token){
  document.getElementById('recordReportButton').addEventListener('click', async () => {
    const blob = await getRecordReportUrl(token);

    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = 'records-report.json';
    link.click();
  });
}

document.addEventListener('DOMContentLoaded', async function () {
  const token = localStorage.getItem('token');

  let currentPage = 1;
  const recordsCount = await getUserRecordsCount(token);
  const recordsPerPage = document.getElementById('recordsPerPageSelect').value;
  await renderPagination(recordsCount, recordsPerPage, currentPage, token);
  await renderRecordTable(token, currentPage);

  const recordsPerPageSelect = document.getElementById('recordsPerPageSelect');
  recordsPerPageSelect.addEventListener('change', async function () {
    const recordsPerPage = parseInt(this.value);
    currentPage = 1;
    const recordsCount = await getUserRecordsCount(token);
    renderPagination(recordsCount, recordsPerPage, currentPage, token);
    await renderRecordTable(token, currentPage);
    setRowLinkListeners();
  });

  document.getElementById('recordsApplyFilters').addEventListener('click', async () => {
    const recordsPerPage = parseInt(document.getElementById('recordsPerPageSelect').value);
    currentPage = 1;
    const recordsCount = await getUserRecordsCount(token);
    renderPagination(recordsCount, recordsPerPage, currentPage, token);
    await renderRecordTable(token, currentPage);
    setRowLinkListeners();
  });

  setRowLinkListeners();

  addActionToActivityLog();
  setUserActivityLogDetails();

  setGenerateReportListener(token);
});
