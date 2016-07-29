(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('week-menu', {
            parent: 'entity',
            url: '/week-menu',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'freeFoodApp.weekMenu.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/week-menu/week-menus.html',
                    controller: 'WeekMenuController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('weekMenu');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('week-menu-detail', {
            parent: 'entity',
            url: '/week-menu/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'freeFoodApp.weekMenu.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/week-menu/week-menu-detail.html',
                    controller: 'WeekMenuDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('weekMenu');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WeekMenu', function($stateParams, WeekMenu) {
                    return WeekMenu.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'week-menu',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('week-menu-detail.edit', {
               parent: 'week-menu-detail',
               url: '/detail/edit',
               data: {
                   authorities: ['ROLE_USER']
               },
               onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                   $uibModal.open({
                       templateUrl: 'app/entities/week-menu/week-menu-dialog.html',
                       controller: 'WeekMenuDialogController',
                       controllerAs: 'vm',
                       backdrop: 'static',
                       size: 'lg',
                       resolve: {
                           entity: ['WeekMenu', function(WeekMenu) {
                               return WeekMenu.get({id : $stateParams.id}).$promise;
                           }]
                       }
                   }).result.then(function() {
                       $state.go('^', {}, { reload: false });
                   }, function() {
                       $state.go('^');
                   });
               }]
           })
        .state('week-menu.new', {
            parent: 'week-menu',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/week-menu/week-menu-dialog.html',
                    controller: 'WeekMenuDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('week-menu', null, { reload: true });
                }, function() {
                    $state.go('week-menu');
                });
            }]
        })
        .state('week-menu.edit', {
            parent: 'week-menu',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/week-menu/week-menu-dialog.html',
                    controller: 'WeekMenuDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WeekMenu', function(WeekMenu) {
                            return WeekMenu.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('week-menu', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('week-menu.delete', {
            parent: 'week-menu',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/week-menu/week-menu-delete-dialog.html',
                    controller: 'WeekMenuDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WeekMenu', function(WeekMenu) {
                            return WeekMenu.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('week-menu', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
