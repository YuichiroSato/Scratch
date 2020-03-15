class CreateUserChatRooms < ActiveRecord::Migration[6.0]
  def change
    create_table :user_chat_rooms do |t|
      t.references :user, null: false, index: false, foreign_key: true
      t.references :chat_room, null: false, index: false, foreign_key: true
      t.datetime :created_at, null: false
    end

    add_index :user_chat_rooms, [ :user_id, :chat_room_id ]
  end
end
