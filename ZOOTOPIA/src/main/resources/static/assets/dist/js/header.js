// Header JavaScript Functions
document.addEventListener('DOMContentLoaded', function() {
    
    // Search functionality
    const searchIcon = document.querySelector('.fas.fa-search');
    if (searchIcon) {
        searchIcon.parentElement.addEventListener('click', function(e) {
            e.preventDefault();
            showSearchModal();
        });
    }
    
    // Cart icon click handler
    const cartIcon = document.querySelector('.fas.fa-shopping-cart');
    if (cartIcon) {
        cartIcon.parentElement.addEventListener('click', function(e) {
            e.preventDefault();
            window.location.href = '/cart';
        });
    }
    
    // Header visibility on scroll
    let lastScrollTop = 0;
    const header = document.querySelector('.navbar');
    
    if (header) {
        window.addEventListener('scroll', function() {
            let scrollTop = window.pageYOffset || document.documentElement.scrollTop;
            
            if (scrollTop > lastScrollTop && scrollTop > 100) {
                // Scrolling down - hide header
                header.style.transform = 'translateY(-100%)';
                header.style.transition = 'transform 0.3s ease-in-out';
            } else {
                // Scrolling up - show header
                header.style.transform = 'translateY(0)';
                header.style.transition = 'transform 0.3s ease-in-out';
            }
            
            lastScrollTop = scrollTop <= 0 ? 0 : scrollTop;
        });
    }
    
    // Initialize header components
    initializeHeaderButtons();
    highlightCurrentPage();
});

// Initialize header buttons visibility
function initializeHeaderButtons() {
    // Ensure login/signup buttons are visible
    const loginBtn = document.querySelector('a[href="/login"]');
    const signupBtn = document.querySelector('a[href="/join"]');
    
    if (loginBtn) {
        loginBtn.style.display = 'inline-block';
        loginBtn.style.visibility = 'visible';
        console.log('Login button found and made visible');
    } else {
        console.warn('Login button not found');
    }
    
    if (signupBtn) {
        signupBtn.style.display = 'inline-block';
        signupBtn.style.visibility = 'visible';
        console.log('Signup button found and made visible');
    } else {
        console.warn('Signup button not found');
    }
    
    // Ensure the right side container is visible
    const rightContainer = document.querySelector('.d-flex.align-items-center');
    if (rightContainer) {
        rightContainer.style.display = 'flex';
        rightContainer.style.visibility = 'visible';
        console.log('Right container found and made visible');
    }
}

// Search modal function
function showSearchModal() {
    const searchTerm = prompt('검색어를 입력하세요:');
    if (searchTerm && searchTerm.trim() !== '') {
        window.location.href = `/search?q=${encodeURIComponent(searchTerm.trim())}`;
    }
}

// Navigation highlighting
function highlightCurrentPage() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link');
    
    navLinks.forEach(link => {
        const linkPath = new URL(link.href).pathname;
        if (linkPath === currentPath) {
            link.style.color = '#ff6b6b';
            link.style.fontWeight = 'bold';
        }
    });
}

// Debug function to check header elements
function debugHeader() {
    console.log('=== Header Debug Info ===');
    console.log('Login button:', document.querySelector('a[href="/login"]'));
    console.log('Signup button:', document.querySelector('a[href="/join"]'));
    console.log('Right container:', document.querySelector('.d-flex.align-items-center'));
    console.log('All buttons:', document.querySelectorAll('.btn'));
    console.log('Navbar:', document.querySelector('.navbar'));
}

// Call debug function after page load
window.addEventListener('load', function() {
    setTimeout(debugHeader, 1000); // Wait 1 second after page load
});
