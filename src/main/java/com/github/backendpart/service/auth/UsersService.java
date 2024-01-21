package com.github.backendpart.service.auth;

import com.github.backendpart.repository.AuthRepository;
import com.github.backendpart.repository.ProfileImageRepository;
import com.github.backendpart.service.mapper.AuthMapper;
import com.github.backendpart.web.dto.users.RequestLoginDto;
import com.github.backendpart.web.dto.users.RequestUserDto;
import com.github.backendpart.web.dto.users.TokenDto;
import com.github.backendpart.web.dto.users.UserDto;
import com.github.backendpart.web.entity.enums.Roles;
import com.github.backendpart.web.entity.users.ProfileImageEntity;
import com.github.backendpart.web.entity.users.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService implements UserDetailsService {
    private final AuthRepository authRepository;
    private final ProfileImageRepository profileImageRepository;
    private final TokenService tokenService;
    private final AuthService authService;
    private String uploadPath;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return authRepository.findByUserId(userId).map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("userId: " + userId + "를 데이터베이스에서 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(UserEntity users){
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(users.getRoles().getType().toString());

        return new User(
                users.getUserId(),
                users.getUserPwd(),
                Collections.singleton(grantedAuthority)
        );
    }

    //이미지를 eager로 불러옴
    public UserEntity findUserByUserId(String userId){
        return authRepository.findByUserIdEagerLoadImage(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 사용자가 존재하지 않습니다."));
    }

    public UserDto getUser(String userId){
        return AuthMapper.INSTANCE.userToUserDto(findUserByUserId(userId));
    }

    @Transactional
    public void saveUser(UserDto userDto){
        authRepository.save(AuthMapper.INSTANCE.userDtoToUser(userDto));
    }

    /**
     * UsernamePasswordAuthenticationToken을 통한 Spring Security인증 진행
     * 이후 tokenService에 userId값을 전달하여 토큰 생성
     * @param requestDTO
     * @return TokenDTO
     */

    @Transactional
    public TokenDto login(RequestLoginDto requestDTO) {
      authService.authenticateLogin(requestDTO);

      UserEntity users = authRepository.findByUserId(requestDTO.getUserId()).get();
      return tokenService.createToken(users);
    }

    @Transactional
    public void signup(RequestUserDto requestDto) {
        if(authRepository.existsByUserId(requestDto.getUserId())) {
          throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        UserEntity user = AuthMapper.INSTANCE.requestUserDtoToUser(requestDto);
        user.updateRole(Roles.ROLE_USER);

        if(!(requestDto.getProfileImage() == null)) {
          ProfileImageEntity userImage = saveUserImage(requestDto.getProfileImage());
          user.updateProfileImage(userImage);
        }

        authRepository.save(user);
    }

    @Transactional
    public ProfileImageEntity saveUserImage(MultipartFile file){
      String fileName = file.getOriginalFilename();
      Path root = Paths.get(uploadPath, "users");

//      try{
//
//      }catch (){
//
//      }
        return null;
    }
}
