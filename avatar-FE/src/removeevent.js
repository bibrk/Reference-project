function removeCustomStyle() {
    document.querySelectorAll('.custom').forEach(item => {
        item.classList.remove('custom');
        item.style.position='inherit'
        })
    }
