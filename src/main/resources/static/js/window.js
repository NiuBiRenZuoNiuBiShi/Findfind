const foods = [
    { name: "寿司", price: "25元", rating: 4.8, location: "一层6号" },
    { name: "汉堡", price: "20元", rating: 4.5, location: "一层7号" },
    { name: "米线", price: "30元", rating: 4.8, location: "一层1号"},
    { name: "炸酱面", price: "25元", rating: 4.7, location: "一层2号" },
    {name: "盖浇饭", price: "20元", rating: 4.7, location: "一层3号" },
    { name: "馄饨", price: "15元", rating: 4.6, location: "一层4号"},
    { name: "水煮鱼", price: "20元", rating: 4.5, location: "一层5号" },
    
];

const modal = document.getElementById("myModal");
const openModalBtn = document.getElementById("openModalBtn");
const closeBtn = document.querySelector(".close");
const randomFoodBtn = document.getElementById("randomFoodBtn");
const foodDisplay = document.getElementById("foodDisplay");

function openModal() {
    modal.style.display = "block";
}
function closeModal() {
    modal.style.display = "none";
}
function showRandomFood() {
    const randomFood = foods[Math.floor(Math.random() * foods.length)];
    foodDisplay.innerHTML = `
        <h1>${randomFood.name}</h1>
        <h3>价格:${randomFood.price}</h3>
        <h3>评分:${randomFood.rating}</h3>
        <h3>地点:${randomFood.location}</h3>
    `;
}

openModalBtn.addEventListener('click', openModal);
closeBtn.addEventListener('click', closeModal);
randomFoodBtn.addEventListener('click', showRandomFood);