

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

  const message = {page: 'Profile', date: new Date()};
  activityLog.push(message);
  sessionStorage.setItem('activityLog', JSON.stringify(activityLog));
}

async function getUserDetails(token) {
  try{
    const response = await fetch('api/v1/user', {
      method: 'GET',
      headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`}
    });

    return await response.json();
  }catch (e){
    window.location.href = '/500-error';
  }
}

async function updateUserDetails(payload, token) {
  const body = {
    id: payload.id
  };
  Object.keys(payload).forEach((field) => {
    if(payload[field]){
      body[field] = payload[field];
    }
  });

  try{
    const response = await fetch('/api/v1/user', {
      method: 'POST',
      headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
      body: JSON.stringify(body)
    });

    return response.json();
  }catch (e) {
    window.location.href = '/500-error';
  }
}

async function deleteUserDetails(id, token){
  try{
    const response = await fetch(`/api/v1/user/${id}`, {
      method: 'DELETE',
      headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`}
    });

    return response.json();
  }catch (e){
    window.location.href = '/500-error';
  }
}

function getFormFields() {
  const profileFirstname = document.getElementById('updateFirstname');
  const profileLastname = document.getElementById('updateLastname');
  const profileCountry = document.getElementById('updateCountry');
  const profileCity = document.getElementById('updateCity');
  const profileAddress = document.getElementById('updateAddress');
  const profilePostalCode = document.getElementById('updatePostalCode');
  const profileAvatarColor = document.getElementById('updateAvatarColor');

  return {profileFirstname, profileLastname, profileCountry, profileCity, profileAddress, profilePostalCode,
    profileAvatarColor };
}

function setFormDetails(payload) {
  const {firstname, lastname, username, role, avatarColor, country, address, city, postalCode} = payload;
  const {profileFirstname, profileLastname, profileCountry, profileCity, profileAddress, profilePostalCode,
    profileAvatarColor } = getFormFields();
  const profileUsername = document.getElementById('profileUsername');
  const profileRole = document.getElementById('profileRole');
  const profileAvatar = document.getElementById('profileAvatar');

  profileAvatar.textContent = `${firstname.substring(0, 1).toUpperCase()} ${lastname.substring(0, 1).toUpperCase()}`;
  profileAvatar.style.backgroundColor = avatarColor || '#00008B';
  profileUsername.textContent = username;
  profileRole.textContent = role;

  profileFirstname.value = firstname;
  profileLastname.value = lastname;
  profileCountry.value = country;
  profileAddress.value = address;
  profileCity.value = city;
  profilePostalCode.value = postalCode;
  profileAvatarColor.value = avatarColor || '#00008B';
}

function setUpdateUserListener(token, user){
  document.getElementById('updateProfileForm').addEventListener('submit', async function (event) {
    const submitButton = document.getElementById('submitButton');
    event.preventDefault();

    const {profileFirstname, profileLastname, profileCountry,
      profileCity, profileAddress, profilePostalCode,
      profileAvatarColor} = getFormFields();

    const payload = {
      id: user.id,
      firstname: profileFirstname.value,
      lastname: profileLastname.value,
      country: profileCountry.value,
      address: profileAddress.value,
      city: profileCity.value,
      postalCode: profilePostalCode.value,
      avatarColor: profileAvatarColor.value
    };

    const result = await updateUserDetails(payload, token);
    setFormDetails(result);
    submitButton.active = false;
  });
}

function setDeleteUserListener(token, user){
  document.getElementById('deleteAccountButton').addEventListener('click', async function (event){
    await deleteUserDetails(user.id, token);

    localStorage.removeItem('token');
    window.location.href = '/login';
  });
}

document.addEventListener('DOMContentLoaded', async function () {
  const token = localStorage.getItem('token');
  const user = await getUserDetails(token);

  setFormDetails(user);
  setUpdateUserListener(token, user);
  setDeleteUserListener(token, user);

  addActionToActivityLog();
  setUserActivityLogDetails();
});
