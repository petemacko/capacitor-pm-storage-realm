
  Pod::Spec.new do |s|
    s.name = 'CapacitorPmStorageRealm'
    s.version = '1.0.9'
    s.summary = 'Realm Plugin'
    s.license = 'MIT'
    s.homepage = 'https://github.com/petemacko/capacitor-pm-storage-realm'
    s.author = 'Pete Macko'
    s.source = { :git => 'https://github.com/petemacko/capacitor-pm-storage-realm', :tag => s.version.to_s }
    s.source_files = 'ios/RealmPlugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
    s.dependency 'CryptoSwift', '~> 1.3.2'
    s.dependency 'RealmSwift', '~> 10.5.0'
  end
