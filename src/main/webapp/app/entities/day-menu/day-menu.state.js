(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('day-menu', {
            parent: 'entity',
            url: '/day-menu',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'freeFoodApp.dayMenu.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/day-menu/day-menus.html',
                    controller: 'DayMenuController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dayMenu');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('day-menu-detail', {
            parent: 'entity',
            url: '/day-menu/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'freeFoodApp.dayMenu.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/day-menu/day-menu-detail.html',
                    controller: 'DayMenuDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dayMenu');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DayMenu', function($stateParams, DayMenu) {
                    return DayMenu.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'day-menu',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('day-menu-detail.edit', {
               parent: 'day-menu-detail',
               url: '/detail/edit',
               data: {
                   authorities: ['ROLE_USER']
               },
               onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                   $uibModal.open({
                       templateUrl: 'app/entities/day-menu/day-menu-dialog.html',
                       controller: 'DayMenuDialogController',
                       controllerAs: 'vm',
                       backdrop: 'static',
                       size: 'lg',
                       resolve: {
                           entity: ['DayMenu', function(DayMenu) {
                               return DayMenu.get({id : $stateParams.id}).$promise;
                           }]
                       }
                   }).result.then(function() {
                       $state.go('^', {}, { reload: false });
                   }, function() {
                       $state.go('^');
                   });
               }]
           })
        .state('day-menu.new', {
            parent: 'day-menu',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day-menu/day-menu-dialog.html',
                    controller: 'DayMenuDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('day-menu', null, { reload: true });
                }, function() {
                    $state.go('day-menu');
                });
            }]
        })
        .state('day-menu.edit', {
            parent: 'day-menu',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day-menu/day-menu-dialog.html',
                    controller: 'DayMenuDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DayMenu', function(DayMenu) {
                            return DayMenu.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('day-menu', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('day-menu.delete', {
            parent: 'day-menu',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/day-menu/day-menu-delete-dialog.html',
                    controller: 'DayMenuDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DayMenu', function(DayMenu) {
                            return DayMenu.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('day-menu', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
