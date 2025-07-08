// ZOOTOPIA 상품 목록 동적 기능 스크립트

document.addEventListener('DOMContentLoaded', function() {
    // 로딩 스피너 표시/숨기기
    function showLoading() {
        const loadingDiv = document.createElement('div');
        loadingDiv.id = 'loading-spinner';
        loadingDiv.innerHTML = `
            <div class="text-center py-5">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
                <p class="mt-2 text-muted">상품을 불러오는 중...</p>
            </div>
        `;
        document.querySelector('.container').appendChild(loadingDiv);
    }

    function hideLoading() {
        const loadingDiv = document.getElementById('loading-spinner');
        if (loadingDiv) {
            loadingDiv.remove();
        }
    }

    // 카테고리 버튼 클릭 시 부드러운 전환
    document.querySelectorAll('.category-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            // 현재 활성화된 버튼 비활성화
            document.querySelectorAll('.category-btn').forEach(btn => {
                btn.classList.remove('btn-primary', 'active');
                btn.classList.add('btn-outline-primary');
            });
            
            // 클릭된 버튼 활성화
            this.classList.remove('btn-outline-primary');
            this.classList.add('btn-primary', 'active');
            
            // 로딩 표시
            showLoading();
        });
    });

    // 검색 폼 제출 시 로딩 표시
    document.querySelector('form').addEventListener('submit', function(e) {
        const searchInput = this.querySelector('input[name="search"]');
        if (searchInput.value.trim()) {
            showLoading();
        }
    });

    // 페이지네이션 클릭 시 로딩 표시
    document.querySelectorAll('.pagination a').forEach(link => {
        link.addEventListener('click', function(e) {
            showLoading();
        });
    });

    // 상품 카드 호버 효과 개선
    document.querySelectorAll('.product-card').forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-8px)';
            this.style.boxShadow = '0 8px 25px rgba(0,0,0,0.15)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(-5px)';
            this.style.boxShadow = '0 4px 15px rgba(0,0,0,0.1)';
        });
    });

    // 좋아요/싫어요 버튼 클릭 이벤트 (향후 AJAX 처리용)
    document.querySelectorAll('.btn-outline-success, .btn-outline-danger').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            
            // 버튼 애니메이션
            this.style.transform = 'scale(1.1)';
            setTimeout(() => {
                this.style.transform = 'scale(1)';
            }, 150);
            
            // 향후 AJAX 처리를 위한 플레이스홀더
            console.log('좋아요/싫어요 버튼 클릭됨');
            
            // 알림 표시
            const toast = document.createElement('div');
            toast.className = 'toast position-fixed top-0 end-0 m-3';
            toast.innerHTML = `
                <div class="toast-body bg-info text-white">
                    <i class="fas fa-info-circle me-2"></i>
                    로그인이 필요합니다!
                </div>
            `;
            document.body.appendChild(toast);
            
            // Bootstrap Toast 표시
            const bsToast = new bootstrap.Toast(toast);
            bsToast.show();
            
            // 3초 후 제거
            setTimeout(() => {
                toast.remove();
            }, 3000);
        });
    });

    // 가격 포맷팅
    document.querySelectorAll('.h5.text-danger').forEach(priceElement => {
        const price = priceElement.textContent.replace('원', '').replace(/,/g, '');
        if (!isNaN(price)) {
            priceElement.textContent = parseInt(price).toLocaleString() + '원';
        }
    });

    // 검색어 하이라이트
    const searchParam = new URLSearchParams(window.location.search).get('search');
    if (searchParam) {
        document.querySelectorAll('.card-title, .card-text').forEach(element => {
            const text = element.textContent;
            const regex = new RegExp(`(${searchParam})`, 'gi');
            if (regex.test(text)) {
                element.innerHTML = text.replace(regex, '<mark>$1</mark>');
            }
        });
    }

    // 스크롤 시 헤더 고정
    window.addEventListener('scroll', function() {
        const navbar = document.querySelector('.navbar');
        if (window.scrollY > 50) {
            navbar.classList.add('navbar-sticky');
        } else {
            navbar.classList.remove('navbar-sticky');
        }
    });

    // 반응형 카테고리 버튼
    function adjustCategoryButtons() {
        const categoryContainer = document.querySelector('.d-flex.justify-content-center.flex-wrap');
        if (window.innerWidth < 768) {
            categoryContainer.classList.add('flex-column');
            categoryContainer.classList.remove('flex-wrap');
        } else {
            categoryContainer.classList.remove('flex-column');
            categoryContainer.classList.add('flex-wrap');
        }
    }

    // 페이지 로드 시 및 화면 크기 변경 시 조정
    adjustCategoryButtons();
    window.addEventListener('resize', adjustCategoryButtons);

    console.log('ZOOTOPIA 상품 목록 동적 기능 초기화 완료');
});
