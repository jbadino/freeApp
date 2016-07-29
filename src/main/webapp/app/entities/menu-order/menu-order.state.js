(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('menu-order', {
            parent: 'entity',
            url: '/menu-order',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'freeFoodApp.menuOrder.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/menu-order/menu-orders.html',
                    controller: 'MenuOrderController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('menuOrder');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('menu-order-detail', {
            parent: 'entity',
            url: '/menu-order/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'freeFoodApp.menuOrder.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/menu-order/menu-order-detail.html',
                    controller: 'MenuOrderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('menuOrder');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MenuOrder', function($stateParams, MenuOrder) {
                    return MenuOrder.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'menu-order',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('menu-order-detail.edit', {
               parent: 'menu-order-detail',
               url: '/detail/edit',
               data: {
                   authorities: ['ROLE_USER']
               },
               onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                   $uibModal.open({
                       templateUrl: 'app/entities/menu-order/menu-order-dialog.html',
                       controller: 'MenuOrderDialogController',
                       controllerAs: 'vm',
                       backdrop: 'static',
                       size: 'lg',
                       resolve: {
                           entity: ['MenuOrder', function(MenuOrder) {
                               return MenuOrder.get({id : $stateParams.id}).$promise;
                           }]
                       }
                   }).result.then(function() {
                       $state.go('^', {}, { reload: false });
                   }, function() {
                       $state.go('^');
                   });
               }]
           })
        .state('menu-order.new', {
            parent: 'menu-order',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/menu-order/menu-order-dialog.html',
                    controller: 'MenuOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('menu-order', null, { reload: true });
                }, function() {
                    $state.go('menu-order');
                });
            }]
        })
        .state('menu-order.edit', {
            parent: 'menu-order',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/menu-order/menu-order-dialog.html',
                    controller: 'MenuOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MenuOrder', function(MenuOrder) {
                            return MenuOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('menu-order', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('menu-order.delete', {
            parent: 'menu-order',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/menu-order/menu-order-delete-dialog.html',
                    controller: 'MenuOrderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MenuOrder', function(MenuOrder) {
                            return MenuOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('menu-order', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
