function mostrarCarregando() {
  document.getElementById('resultado').textContent = "🔄 Carregando...";
}

async function chamarApiFato() {
  try {
    mostrarCarregando();
    const resposta = await fetch('/api/fato');
    const dados = await resposta.json();
    document.getElementById('resultado').textContent = `🐾 ${dados.fato}`;
  } catch (erro) {
    document.getElementById('resultado').textContent = "❌ Erro ao buscar fato de gato: " + erro;
  }
}

async function chamarApiCep() {
  const cep = document.getElementById('cepInput').value.trim();
  if (!cep) {
    document.getElementById('resultado').textContent = "⚠️ Por favor, digite um CEP.";
    return;
  }

  try {
    mostrarCarregando();
    const resposta = await fetch(`/api/cep/${cep}`);
    const dados = await resposta.text();
    document.getElementById('resultado').textContent = dados;
  } catch (erro) {
    document.getElementById('resultado').textContent = "❌ Erro ao buscar CEP: " + erro;
  }
}

async function chamarApiConselho() {
  try {
    mostrarCarregando();
    const resposta = await fetch('/api/conselho');
    const dados = await resposta.text();
    document.getElementById('resultado').textContent = dados;
  } catch (erro) {
    document.getElementById('resultado').textContent = "❌ Erro ao buscar conselho: " + erro;
  }
}
