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

function addActionToActivityLog(budgetId){
  const sessionActivityLog = sessionStorage.getItem('activityLog');
  const activityLog = JSON.parse(sessionActivityLog) || [];

  const message = {page: `Budget ${budgetId} details`, date: new Date()};
  activityLog.push(message);
  sessionStorage.setItem('activityLog', JSON.stringify(activityLog));
}

async function getRecordCategories(token) {
  try{
    const response = await fetch('http://app.budgetmate.com/api/v1/record/record-categories', {
      method: 'GET',
      headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`}
    });

    return response.json();
  }catch (e){
    window.location.href = '/500-error';
  }
}

async function getUserBudget(token, id){
  try{
    const response = await fetch(`http://app.budgetmate.com/api/v1/budget/${id}`, {
      method: 'GET',
      headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`}
    });


    return response.json();
  }catch (e){
    window.location.href = '/500-error';
  }
}

async function deleteUserBudget(token, id) {
  try{
    const response =  await fetch(`http://app.budgetmate.com/api/v1/budget/${id}`, {
      method: 'DELETE',
      headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`}
    });
  }catch (e) {
    window.location.href = '/500-error';
  }

}

async function setBudgetDetails(token, id){
  const budget = await getUserBudget(token, id);
  const recordCategories = await getRecordCategories(token);

  const budgetName = document.getElementById('budgetName');
  const budgetSelectedRecordCategories = document.getElementById('budgetSelectedRecordCategories');
  const updateBudgetName = document.getElementById('updateBudgetName');
  const updateBudgetAmount = document.getElementById('updateBudgetAmount');

  budgetName.textContent = budget.name;
  updateBudgetName.value = budget.name;
  updateBudgetAmount.value = budget.amount;
  renderRecordCategories(recordCategories, budget.recordCategories);

  budgetSelectedRecordCategories.innerHTML = budget.recordCategories.map(category =>
    `<span class="text-lg alert-info border border-radius1 p-1 ml-1">${category.name}</span>`
  ).join('');
}

async function setSubmitBudgetListener(token, id){
  document.getElementById('updateBudgetForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const submitButton = document.getElementById('submitBudgetButton');
    const name = document.getElementById('updateBudgetName').value;
    const amount = document.getElementById('updateBudgetAmount').value;
    const recordCategories = Array
      .from(document.querySelectorAll('#updateBudgetRecordCategoriesDropdown .form-check-input:checked'))
      .map(input => input.value);

    try{
      const response = await fetch(`http://app.budgetmate.com/api/v1/budget/${id}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
        body: JSON.stringify({ amount, name, recordCategories })
      });
    }catch (e) {
      window.location.href = '/500-error';
    }


    await setBudgetDetails(token, id);
    submitButton.blur();
  });
}

function setDeleteBudgetListener(token, id){
  document.getElementById('deleteBudgetButton').addEventListener('click', async () => {
    await deleteUserBudget(token, id);

    window.location.href = '/budgets';
  });
}

function renderRecordCategories(recordCategories, selectedRecordCategories){
  const updateBudgetRecordCategoriesDropdown = document.getElementById('updateBudgetRecordCategoriesDropdown');

  updateBudgetRecordCategoriesDropdown.innerHTML = '';
  recordCategories.forEach((recordCategory) => {
    let input;
    if(selectedRecordCategories.find((selectRecordCategory) => selectRecordCategory.id === recordCategory.id)) {
      input = `<input checked type="checkbox" class="form-check-input" name="${recordCategory.name}" 
                    value="${recordCategory.name}"/>`;
    }else {
      input = `<input type="checkbox" class="form-check-input" name="${recordCategory.name}" value="${recordCategory.name}"/>`;
    }


    updateBudgetRecordCategoriesDropdown.innerHTML +=
            `<div class="form-check">
                    ${input}
                    <label class="mr-2">${recordCategory.name}</label>
            </div>`;
  });
}

document.addEventListener('DOMContentLoaded', async function () {
  const token = localStorage.getItem('token');
  const budgetId = window.location.pathname.split('/').pop();

  await setBudgetDetails(token, budgetId);
  setSubmitBudgetListener(token, budgetId);
  setDeleteBudgetListener(token, budgetId);

  addActionToActivityLog(budgetId);
  setUserActivityLogDetails();
});
