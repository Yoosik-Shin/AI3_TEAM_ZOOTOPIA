// Products List JavaScript

// 좋아요 기능
function likeProduct(productNo) {
    fetch(`/products/like/${productNo}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // 좋아요 수 업데이트
            const likeCount = document.querySelector(`[data-product="${productNo}"] .likes-count`);
            if (likeCount) {
                likeCount.textContent = data.likes;
            }
            
            // 좋아요 버튼 애니메이션
            const heartBtn = document.querySelector(`[onclick="likeProduct(${productNo})"]`);
            if (heartBtn) {
                heartBtn.classList.add('liked');
                setTimeout(() => {
                    heartBtn.classList.remove('liked');
                }, 300);
            }
            
            // 토스트 메시지 (선택사항)
            showToast('좋아요가 추가되었습니다!');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('오류가 발생했습니다.', 'error');
    });
}

// 토스트 메시지 표시
function showToast(message, type = 'success') {
    // 기존 토스트 제거
    const existingToast = document.querySelector('.toast-notification');
    if (existingToast) {
        existingToast.remove();
    }
    
    // 새 토스트 생성
    const toast = document.createElement('div');
    toast.className = `toast-notification toast-${type}`;
    toast.textContent = message;
    
    // 스타일 적용
    toast.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: ${type === 'success' ? '#28a745' : '#dc3545'};
        color: white;
        padding: 12px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.3);
        z-index: 9999;
        transform: translateX(400px);
        transition: transform 0.3s ease;
        font-size: 0.9rem;
        font-weight: 500;
    `;
    
    document.body.appendChild(toast);
    
    // 애니메이션
    setTimeout(() => {
        toast.style.transform = 'translateX(0)';
    }, 100);
    
    // 자동 제거
    setTimeout(() => {
        toast.style.transform = 'translateX(400px)';
        setTimeout(() => {
            toast.remove();
        }, 300);
    }, 3000);
}

// 좋아요 버튼 애니메이션 CSS 추가
const style = document.createElement('style');
style.textContent = `
    .btn-heart.liked {
        animation: heartBeat 0.3s ease;
        background: #ff5a8a !important;
    }
    
    @keyframes heartBeat {
        0% { transform: scale(1); }
        50% { transform: scale(1.2); }
        100% { transform: scale(1); }
    }
    
    .toast-notification {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }
`;
document.head.appendChild(style);

// 이미지 로드 에러 처리
document.addEventListener('DOMContentLoaded', function() {
    const productImages = document.querySelectorAll('.product-image img');
    
    productImages.forEach(img => {
        img.addEventListener('error', function() {
            this.src = '/img/default-thumbnail.png';
            this.alt = '이미지를 불러올 수 없습니다';
        });
    });
    
    // 검색 입력시 엔터키 처리
    const searchInput = document.querySelector('.search-input');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                this.closest('form').submit();
            }
        });
    }
});

// 무한 스크롤 (선택사항)
let isLoading = false;
let currentPage = 1;

function loadMoreProducts() {
    if (isLoading) return;
    
    isLoading = true;
    currentPage++;
    
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set('page', currentPage);
    
    fetch(`/products/list?${urlParams.toString()}`)
        .then(response => response.text())
        .then(html => {
            // HTML 파싱하여 새로운 상품 카드들만 추출
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const newCards = doc.querySelectorAll('.col-lg-4.col-md-6.mb-4');
            
            if (newCards.length > 0) {
                const productGrid = document.querySelector('.row');
                newCards.forEach(card => {
                    productGrid.appendChild(card);
                });
            }
            
            isLoading = false;
        })
        .catch(error => {
            console.error('Error loading more products:', error);
            isLoading = false;
        });
}

// 스크롤 이벤트 (무한 스크롤 활성화 시)
// window.addEventListener('scroll', function() {
//     if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 1000) {
//         loadMoreProducts();
//     }
// });
