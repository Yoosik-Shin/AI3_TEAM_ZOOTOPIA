// Header JavaScript Functions
document.addEventListener('DOMContentLoaded', function() {
    
    // Mobile menu toggle functionality
    const mobileMenuToggle = document.querySelector('.mobile-menu-toggle');
    const mobileNav = document.querySelector('.mobile-nav');
    
    if (mobileMenuToggle && mobileNav) {
        mobileMenuToggle.addEventListener('click', function() {
            mobileNav.classList.toggle('active');
            
            // Change icon based on menu state
            const icon = this.querySelector('i');
            if (mobileNav.classList.contains('active')) {
                icon.classList.remove('fa-bars');
                icon.classList.add('fa-times');
            } else {
                icon.classList.remove('fa-times');
                icon.classList.add('fa-bars');
            }
        });
    }
    
    // Search functionality
    const searchIcon = document.querySelector('.search-icon');
    if (searchIcon) {
        searchIcon.addEventListener('click', function(e) {
            e.preventDefault();
            // 검색 모달 또는 검색 페이지로 이동
            showSearchModal();
        });
    }
    
    // Cart icon click handler
    const cartIcon = document.querySelector('.cart-icon');
    if (cartIcon) {
        cartIcon.addEventListener('click', function(e) {
            e.preventDefault();
            // 장바구니 페이지로 이동
            window.location.href = '/cart';
        });
    }
    
    // Header scroll effect
    let lastScrollTop = 0;
    const header = document.querySelector('.zootopia-header');
    
    window.addEventListener('scroll', function() {
        let scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        
        if (scrollTop > lastScrollTop && scrollTop > 100) {
            // Scrolling down
            header.style.transform = 'translateY(-100%)';
        } else {
            // Scrolling up
            header.style.transform = 'translateY(0)';
        }
        
        lastScrollTop = scrollTop <= 0 ? 0 : scrollTop;
    });
    
    // Close mobile menu when clicking outside
    document.addEventListener('click', function(e) {
        if (mobileNav && mobileNav.classList.contains('active')) {
            if (!mobileNav.contains(e.target) && !mobileMenuToggle.contains(e.target)) {
                mobileNav.classList.remove('active');
                const icon = mobileMenuToggle.querySelector('i');
                icon.classList.remove('fa-times');
                icon.classList.add('fa-bars');
            }
        }
    });
    
    // Update cart badge count
    updateCartBadge();
});

// Search modal function
function showSearchModal() {
    // 간단한 프롬프트 검색 (실제로는 모달 구현)
    const searchTerm = prompt('검색어를 입력하세요:');
    if (searchTerm && searchTerm.trim() !== '') {
        // 검색 결과 페이지로 이동
        window.location.href = `/search?q=${encodeURIComponent(searchTerm.trim())}`;
    }
}

// Update cart badge count
function updateCartBadge() {
    // 로컬 스토리지 또는 서버에서 장바구니 아이템 수 가져오기
    const cartCount = getCartItemCount();
    const cartBadge = document.querySelector('.cart-badge');
    
    if (cartBadge) {
        if (cartCount > 0) {
            cartBadge.textContent = cartCount > 99 ? '99+' : cartCount;
            cartBadge.style.display = 'flex';
        } else {
            cartBadge.style.display = 'none';
        }
    }
}

// Get cart item count (예시 함수)
function getCartItemCount() {
    // 실제로는 서버 API 호출 또는 로컬 스토리지에서 가져오기
    const cartItems = localStorage.getItem('cartItems');
    if (cartItems) {
        try {
            const items = JSON.parse(cartItems);
            return Array.isArray(items) ? items.length : 0;
        } catch (e) {
            return 0;
        }
    }
    return 0;
}

// Navigation highlighting
function highlightCurrentPage() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-links a');
    
    navLinks.forEach(link => {
        const linkPath = new URL(link.href).pathname;
        if (linkPath === currentPath) {
            link.style.color = '#ff6b6b';
            link.style.backgroundColor = 'rgba(255, 107, 107, 0.1)';
        }
    });
}

// Initialize when page loads
window.addEventListener('load', function() {
    highlightCurrentPage();
});
