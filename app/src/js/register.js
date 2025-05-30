document.addEventListener('DOMContentLoaded', function () {
  const form = document.getElementById('registerForm');
  const errorModal = new bootstrap.Modal(document.getElementById('errorModal'));
  const errorModalBody = document.getElementById('errorModal-body');
  const emailModal = new bootstrap.Modal(document.getElementById('emailModal'));

  function showError(text) {
    errorModalBody.textContent = text;
    errorModal.show();
  }

  function getFormFields () {
    const firstname = document.getElementById('inputFirstName').value;
    const lastname = document.getElementById('inputLastName').value;
    const email = document.getElementById('inputEmail').value;
    const password = document.getElementById('inputPassword').value;
    const repeatPassword = document.getElementById('inputRepeatPassword').value;
    const receiveNewsLetters = document.getElementById('inputNewsLetter').checked;
    const termsCheckbox = document.getElementById('inputTermsConditions').checked;

    return {firstname, lastname, email, password, receiveNewsLetters, repeatPassword, termsCheckbox};
  }

  async function submitForm(){
    const {firstname, lastname, email, password, receiveNewsLetters} = getFormFields();

    const response = await fetch('/api/v1/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ firstname, lastname, email, password, receiveNewsLetters })
    });

    if (response.ok) {
      emailModal.show();
      setTimeout(() => { window.location.href = '/login'; }, 1000 * 10);
    } else {
      const errorData = await response.json();
      showError(errorData.message);
    }
  }

  form.addEventListener('submit', async function (event) {
    event.preventDefault();

    const {password, repeatPassword, termsCheckbox} = getFormFields();
    if (password !== repeatPassword) {
      showError('Repeat password correctly!');
      return;
    }
    if (!termsCheckbox) {
      showError('You must agree to the terms and conditions.');
      return;
    }

    try {
      await submitForm();
    } catch (error) {
      console.error('Fetch failed:', error);
      showError(error);
    }
  });
});
