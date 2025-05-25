const originIds = ["sidebar.dashboard", "sidebar.recordHistory", "sidebar.budgets", "sidebar.analytics", "sidebar.profile",
    "profile", "profile.firstname", "profile.lastname", "profile.country", "profile.address", "profile.city",
    "profile.postalCode", "profile.avatarColor", "profile.save", "deleteAccount"]

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

    const message = {page: `Profile`, date: new Date()}
    activityLog.push(message)
    sessionStorage.setItem('activityLog', JSON.stringify(activityLog))
}

async function getUserDetails(token) {
    return (await fetch('api/v1/user', {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
    })).json()
}

async function updateUserDetails(token, payload) {
    Object.keys(payload).forEach(field => !payload[field] && delete payload[field])

    return (await fetch(`/api/v1/user/${id}`, {
        method: 'PATCH',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`},
        body: JSON.stringify(payload)
    })).json()
}

async function deleteUserDetails(id, token){
    return (await fetch(`/api/v1/user/${id}`, {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${token}`}
    })).json()
}

function getFormFields() {
    const profileFirstname = document.getElementById('updateFirstname')
    const profileLastname = document.getElementById('updateLastname')
    const profileCountry = document.getElementById('updateCountry')
    const profileCity = document.getElementById('updateCity')
    const profileAddress = document.getElementById('updateAddress')
    const profilePostalCode = document.getElementById('updatePostalCode')
    const profileAvatarColor = document.getElementById('updateAvatarColor')

    return { profileFirstname, profileLastname, profileCountry, profileCity, profileAddress, profilePostalCode,
        profileAvatarColor }
}

function setFormDetails(payload) {
    const {firstname, lastname, username, role, avatarColor, country, address, city, postalCode} = payload
    const {profileFirstname, profileLastname, profileCountry, profileCity, profileAddress, profilePostalCode,
        profileAvatarColor } = getFormFields()
    const profileUsername = document.getElementById('profileUsername')
    const profileRole = document.getElementById('profileRole')
    const profileAvatar = document.getElementById('profileAvatar')

    profileAvatar.textContent = `${firstname.substring(0, 1).toUpperCase()} ${lastname.substring(0, 1).toUpperCase()}`
    profileAvatar.style.backgroundColor = avatarColor || '#00008B'
    profileUsername.textContent = username
    profileRole.textContent = role

    profileFirstname.value = firstname
    profileLastname.value = lastname
    profileCountry.value = country
    profileAddress.value = address
    profileCity.value = city
    profilePostalCode.value = postalCode
    profileAvatarColor.value = avatarColor || '#00008B'
}

function setUpdateUserListener(token, user){
    document.getElementById('updateProfileForm').addEventListener('submit', async function (event) {
        const submitButton = document.getElementById('submitButton')
        event.preventDefault();

        const {profileFirstname, profileLastname, profileCountry,
            profileCity, profileAddress, profilePostalCode,
            profileAvatarColor} = getFormFields()

        const payload = {
            id: user.id,
            firstname: profileFirstname.value,
            lastname: profileLastname.value,
            country: profileCountry.value,
            address: profileAddress.value,
            city: profileCity.value,
            postalCode: profilePostalCode.value,
            avatarColor: profileAvatarColor.value
        }

        const result = await updateUserDetails(token, payload)
        setFormDetails(result)
        submitButton.active = false
    })
}

function setDeleteUserListener(token, user){
    document.getElementById("deleteAccountButton").addEventListener("click", async function (){
        await deleteUserDetails(user.id, token)

        localStorage.removeItem('token')
        window.location.href = '/login'
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
    const user = await getUserDetails(token)

    setFormDetails(user)
    setUpdateUserListener(token, user)
    setDeleteUserListener(token, user)

    addActionToActivityLog()
    setUserActivityLogDetails()

    await renderGeneralUIByLanguage(token, lang)
    setEnglishLanguageSelectorListener(token)
    setDutchLanguageSelectorListener(token)
})