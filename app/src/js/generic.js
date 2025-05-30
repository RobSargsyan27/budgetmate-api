function setUserLogOutListener() {
  const logOutButton = document.getElementById('logOutButton');

  logOutButton.addEventListener('click', function () {
    localStorage.removeItem('token');
    sessionStorage.removeItem('activityLog');
    window.location.href = '/login';
  });
}

async function validateToken(token) {
  try {
    const response = await fetch('http://app.budgetmate.com/api/v1/auth/validate-token', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({token})
    });

    if (response.ok) {
      const {isTokenValid} = await response.json();
      if (!isTokenValid) {
        window.location.href = '/login';
      }

    } else {
      window.location.href = '/login';
    }
  } catch (error) {
    window.location.href = '/login';
  }
}

async function getIdsTranslation(token, lang, body) {
  const response = await fetch(`http://app.budgetmate.com/api/v1/translate?lang=${lang}`, {
    method: 'POST',
    headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
    body: JSON.stringify(body)
  });

  return response.json();
}

async function getUserDetails(token) {
  const response = await fetch('http://app.budgetmate.com/api/v1/user', {
    method: 'GET',
    headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`}
  });

  return response.json();
}

async function getNotifications(token) {
  const response = await fetch('http://app.budgetmate.com/api/v1/user/notifications', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });
  const responseBody = await response.json();

  const topNavbarNotificationCount = document.getElementById('topNavbarNotificationCount');
  const dropdownMenu = document.getElementById('topNavbarNotifications');
  dropdownMenu.innerHTML = '<h6 class="dropdown-header">Notifications Center</h6>';


  if (responseBody.length) {
    topNavbarNotificationCount.textContent = responseBody.length;

    responseBody.forEach(notification => {
      const alertHTML = `
                <a class="dropdown-item d-flex flex-column" href="#" id="${notification.id}-link">
                <div class="d-flex flex-row">
                        <div class="icon-circle bg-primary mr-3">
                            <i class="fas fa-info text-white"></i>
                        </div>
                        <div class="text-gray-500">
                        "${notification.requestedUsername}" requested to add "${notification.accountName}" account.
                        </div>
                </div>
                <div class="d-flex flex-row justify-content-between mt-2">
                    <div class="btn btn-sm btn-danger notification-reject-button" id="${notification.id}">Reject</div>
                    <div class="btn btn-sm btn-primary notification-approve-button" id="${notification.id}">Accept</div>
                </div>
                </a>`;
      dropdownMenu.innerHTML += alertHTML;
    });
  }


  const rejectButtons = Array.from(document.getElementsByClassName('notification-reject-button'));
  rejectButtons.forEach((button) => {
    button.addEventListener('click', async (event) => {
      try {
        await fetch(`http://app.budgetmate.com/api/v1/account/${event.target.id}/false`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });
      } catch (error) {
        console.log(error);
      } finally {
        const link = document.getElementById(`${event.target.id}-link`);
        if (link) {
          link.remove();
        }
        if (parseInt(topNavbarNotificationCount.textContent) - 1 === 0) {
          topNavbarNotificationCount.style.display = 'none';
        } else {
          topNavbarNotificationCount.textContent = (parseInt(topNavbarNotificationCount.textContent) - 1).toString();
        }
      }
    });
  });

  const approvedButtons = Array.from(document.getElementsByClassName('notification-approve-button'));
  approvedButtons.forEach((button) => {
    button.addEventListener('click', async (event) => {
      try {
        await fetch(`http://app.budgetmate.com/api/v1/account/${event.target.id}/true`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });
      } catch (error) {
        console.log(error);
      } finally {
        const link = document.getElementById(`${event.target.id}-link`);
        if (link) {
          link.remove();
        }
        if (parseInt(topNavbarNotificationCount.textContent) - 1 === 0) {
          topNavbarNotificationCount.style.display = 'none';
        } else {
          topNavbarNotificationCount.textContent = (parseInt(topNavbarNotificationCount.textContent) - 1).toString();
        }
      }
    });
  });
}

function setTopNavDetails(username, firstname, lastname, avatarColor) {
  const topNavUsername = document.getElementById('topNavUsername');
  const topNavAvatar = document.getElementById('topNavAvatar');

  topNavUsername.textContent = username;
  topNavAvatar.textContent = `${firstname.substring(0, 1).toUpperCase()} ${lastname.substring(0, 1).toUpperCase()}`;
  topNavAvatar.style.backgroundColor = avatarColor || '#00008B';

  setUserActivityLogDetails();
}

document.addEventListener('DOMContentLoaded', async function () {
  const token = localStorage.getItem('token');
  const body = document.getElementById('page-top');

  if (!token) {
    window.location.href = '/login';
  }

  await validateToken(token);

  setUserLogOutListener();
  const user = await getUserDetails(token);
  await getNotifications(token);
  setTopNavDetails(user.username, user.firstname, user.lastname, user.avatarColor);

  body.style.display = 'block';
});
